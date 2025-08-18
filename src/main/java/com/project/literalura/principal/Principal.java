package com.project.literalura.principal;

import com.project.literalura.model.dto.GutendexResponse;
import com.project.literalura.model.dto.GutenBookDto;
import com.project.literalura.service.ConsumoApi;
import com.project.literalura.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Component
public class Principal {

    private final Scanner read = new Scanner(System.in);
    private final ConsumoApi consumo;
    private final ConverteDados conversor;

    private final String base = "https://gutendex.com/books";

    public Principal(ConsumoApi consumo, ConverteDados conversor) {
        this.consumo = consumo;
        this.conversor = conversor;
    }

    public void exibeMenu() {
        int option = -1;

        while (option != 0) {
            var menu = """
                  =================================================
                                    CATÁLOGO
                  =================================================

                  1 - Buscar livro pelo título
                  2 - Listar livros registrados
                  3 - Listar autores registrados
                  4 - Listar autores vivos em determinado ano
                  5 - Listar livros de determinado idioma
                  6 - Listar Top livros mais baixados
                  7 - Pesquisar por autores registrados

                  0 - Sair

                  =================================================
                  """;

            System.out.println(menu);
            option = safeInt();

            switch (option) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> System.out.println("TODO: listar livros do banco");
                case 3 -> System.out.println("TODO: listar autores do banco");
                case 4 -> System.out.println("TODO: autores vivos em ano X");
                case 5 -> System.out.println("TODO: livros por idioma");
                case 6 -> System.out.println("TODO: top downloads");
                case 7 -> System.out.println("TODO: pesquisar autores registrados");
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private int safeInt() {
        while (!read.hasNextInt()) {
            System.out.println("Digite um número válido:");
            read.next();
        }
        int v = read.nextInt();
        read.nextLine(); // consome \n
        return v;
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite parte do título: ");
        String termo = read.nextLine().trim();
        if (termo.isBlank()) {
            System.out.println("Termo vazio.");
            return;
        }

        String query = URLEncoder.encode(termo, StandardCharsets.UTF_8);
        String url = base + "?search=" + query;

        String json = consumo.obterDados(url);
        GutendexResponse resp = conversor.obterDados(json, GutendexResponse.class);

        if (resp == null || resp.results() == null || resp.results().isEmpty()) {
            System.out.println("Nenhum livro encontrado para: " + termo);
            return;
        }

        System.out.println("\nResultados:");
        for (GutenBookDto b : resp.results()) {
            String autor = (b.authors() != null && !b.authors().isEmpty())
                    ? b.authors().get(0).name() : "Autor desconhecido";

            System.out.printf("- [%d] %s | Autor: %s | Idiomas: %s | Downloads: %s%n",
                    b.id(), b.title(), autor, b.languages(), b.download_count());
        }
        System.out.println();
    }
}