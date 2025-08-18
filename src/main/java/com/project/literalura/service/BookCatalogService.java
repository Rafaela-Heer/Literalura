package com.project.literalura.service;

import com.project.literalura.model.dto.GutendexResponse;
import com.project.literalura.model.dto.GutenAuthor;
import com.project.literalura.model.dto.GutenBook;
import com.project.literalura.model.entity.Author;
import com.project.literalura.model.entity.Book;
import com.project.literalura.repository.AuthorRepository;
import com.project.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class BookCatalogService {

    private final ConsumoApi consumoApi;
    private final ConverteDados conversor;
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    private static final String BASE = "https://gutendex.com/books/";

    public BookCatalogService(ConsumoApi consumoApi, ConverteDados conversor,
                              BookRepository bookRepo, AuthorRepository authorRepo) {
        this.consumoApi = consumoApi;
        this.conversor = conversor;
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @Transactional
    public int importBySearch(String term) {
        return importByFilters(term, null);
    }

    @Transactional
    public int importByFilters(String term, String languagesCsv) {
        String url = buildUrl(term, languagesCsv);

        String json = consumoApi.obterDados(url);
        GutendexResponse resp = conversor.obterDados(json, GutendexResponse.class);

        int found = (resp != null && resp.getResults() != null) ? resp.getResults().size() : 0;
        if (found == 0) {
            System.out.println("Nenhum resultado encontrado no Gutendex para(Dica: o catálogo é só de domínio público. Exemplos que funcionam:'sherlock', 'alice', 'dracula' (en), 'Machado de Assis', 'Memórias Póstumas', 'Os Lusíadas' (pt)) "
                    + (term != null && !term.isBlank() ? "search='" + term + "' " : "")
                    + (languagesCsv != null && !languagesCsv.isBlank() ? "languages='" + languagesCsv + "'" : ""));
            return 0;
        }

        int saved = 0;
        for (GutenBook gb : resp.getResults()) {

            if (bookRepo.findByGutenId(gb.getId()).isPresent()) continue;

            Book b = new Book();
            b.setGutenId(gb.getId());
            b.setTitle(gb.getTitle());
            b.setDownloadCount(gb.getDownloadCount());
            if (gb.getLanguages() != null) {
                b.getLanguages().addAll(gb.getLanguages());
            }

            if (gb.getAuthors() != null && !gb.getAuthors().isEmpty()) {
                GutenAuthor ga = gb.getAuthors().get(0);
                Author author = authorRepo.findByNameIgnoreCase(ga.getName())
                        .orElseGet(() -> {
                            Author a = new Author();
                            a.setName(ga.getName());
                            a.setBirthYear(ga.getBirthYear());
                            a.setDeathYear(ga.getDeathYear());
                            return authorRepo.save(a);
                        });
                b.getAuthors().add(author);
            }
            bookRepo.save(b);
            saved++;
        }

        System.out.printf("Encontrados na API: %d | Novos salvos: %d%n", found, saved);
        return saved;
    }


    private String buildUrl(String term, String languagesCsv) {
        StringBuilder sb = new StringBuilder(BASE);
        boolean first = true;

        if (term != null && !term.isBlank()) {
            sb.append(first ? "?" : "&")
                    .append("search=")
                    .append(URLEncoder.encode(term, StandardCharsets.UTF_8));
            first = false;
        }
        if (languagesCsv != null && !languagesCsv.isBlank()) {
            String langs = languagesCsv.replaceAll("\\s+", "");
            sb.append(first ? "?" : "&")
                    .append("languages=")
                    .append(URLEncoder.encode(langs, StandardCharsets.UTF_8));
        }
        return sb.toString();
    }
}
