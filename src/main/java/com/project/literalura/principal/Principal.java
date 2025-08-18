package com.project.literalura.principal;

import com.project.literalura.model.entity.Book;
import com.project.literalura.repository.BookRepository;
import com.project.literalura.repository.AuthorRepository;
import com.project.literalura.service.BookCatalogService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal{

    private final Scanner read = new Scanner(System.in);
    private final BookCatalogService catalog;
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    public Principal(BookCatalogService catalog, BookRepository bookRepo, AuthorRepository authorRepo) {
        this.catalog = catalog;
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public void exibeMenu() {
        int option = -1;
        while (option != 0) {
            System.out.print("""
                  ============================
                        CATÁLOGO
                  ============================

                  1 - Buscar livro pelo título
                  2 - Listar livros
                  3 - Lista autores
                  4 - Listar autores em determinado ano
                  5 - Listar livros por idioma
                  6 - Top 10 livros por downloads
                  7 - Buscar livros por autor registrado
                  
                  0 - Sair
                  
                  ->
                  """);
            option = safeInt();

            switch (option) {
                case 1 -> buscarESalvar();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listaAnoAutoresVivos();
                case 5 -> listarPorIdioma();
                case 6 -> listarTopDownloads();
                case 7 -> buscarPorAutorRegistrado();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private int safeInt() {
        while (!read.hasNextInt()) {
            System.out.println("Digite um número válido: \n -> ");
            read.next();
        }
        int v = read.nextInt();
        read.nextLine();
        return v;
    }

    private int readYear() {
        while (true) {
            System.out.print("Informe o ano (ex: 1580): ");
            if (!read.hasNextInt()) { read.next(); System.out.println("Ano inválido."); continue; }
            int y = read.nextInt(); read.nextLine();
            int current = java.time.Year.now().getValue();
            if (y < 0 || y > current) { System.out.println("Ano deve estar entre 0 e " + current + "."); continue; }
            return y;
        }
    }

    private void buscarESalvar() {
        System.out.print("Digite parte do título ou autor: ");
        String termo = read.nextLine().trim();

        System.out.print("Filtrar por idioma(s)? (ex: en, fr ou pt) — deixe vazio para ignorar: ");
        String langs = read.nextLine().trim();
        if (langs.isBlank()) langs = null;

        int salvos = catalog.importByFilters(termo, langs);

        if (salvos == 0) {
            System.out.println("Nenhum livro novo foi salvo. "
                    + "Motivos comuns: nada encontrado na API ou já estavam no banco.");
        }

        bookRepo.findAll().stream()
                .limit(10)
                .forEach(b -> System.out.println("- " + b.getTitle()));

        var recentes = bookRepo.findRecentByTitleLike(termo);
        if (recentes.isEmpty()) {
            System.out.println("Nenhum título contendo: '" + termo + "' foi encontrado no banco. \nDica: o catálogo é só de domínios públicos. ");
        } else {
            System.out.println("\nTítulos contendo '" + termo + "':");
            recentes.stream().limit(10).forEach(b -> System.out.println("- " + b.getTitle()));
        }
    }

    private void listarLivros() {
        List<Book> books = bookRepo.findAll();
        if (books.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
            return;
        }
        books.forEach(b -> System.out.println("- " + b));
    }

    private void listarAutores() {
        var authors = authorRepo.findAllByOrderByNameAsc(); // ou authorRepo.findAll()
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
            return;
        }
        System.out.println("\nAutores registrados:");
        authors.forEach(a -> System.out.println("- " + a));
        System.out.println();
    }
    private void listaAnoAutoresVivos() {
        System.out.print("Informe o ano (ex: 1900): ");
        Integer year = readYear();

        var vivos = authorRepo.findAliveInYear(year);
        if (vivos.isEmpty()) {
            System.out.println("Nenhum autor vivo em " + year + ".");
            return;
        }
        System.out.println("\nAutores vivos em " + year + ":");
        vivos.forEach(a -> System.out.println("- " + a));
        System.out.println();
    }

    private void listarPorIdioma() {
        System.out.print("Informe o código do idioma (ex.: en, pt, es ou fr): ");
        String lang = read.nextLine().trim();
        if (lang.isBlank()) {
            System.out.println("Idioma vazio.");
            return;
        }
        long total = bookRepo.countByLanguage(lang);
        var livros = bookRepo.findByLanguage(lang);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + lang);
            return;
        }
        System.out.println("\nLivros no idioma '" + lang + "' (total: " + total + "):");
        livros.forEach(b ->
                System.out.printf("- [%d] %s | Idiomas: %s | Downloads: %s%n",
                        b.getGutenId(), b.getTitle(), b.getLanguages(), b.getDownloadCount())
        );
        System.out.println();
    }

    private void listarTopDownloads() {
        var top = bookRepo.findTop10ByOrderByDownloadCountDesc();
        if (top.isEmpty()) {
            System.out.println("Não há livros registrados.");
            return;
        }
        System.out.println("\nTop 10 livros por downloads: ");
        top.forEach(b ->
                System.out.printf("- %s (downloads: %s)%n",
                        b.getTitle(), b.getDownloadCount())
        );
        System.out.println();
    }

    private void buscarPorAutorRegistrado() {
        System.out.print("Digite parte do nome do autor: \n-> ");
        String nome = read.nextLine().trim();
        if (nome.isBlank()) {
            System.out.println("Nome vazio.");
            return;
        }
        var livros = bookRepo.findByAuthorName(nome);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para autor contendo: " + nome);
            return;
        }
        System.out.println("\nLivros de autores que correspondem a '" + nome + "':");
        livros.forEach(b ->
                System.out.printf("- %s | Idiomas: %s | Downloads: %s%n",
                        b.getTitle(), b.getLanguages(), b.getDownloadCount())
        );
        System.out.println();
    }
}
