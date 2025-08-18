package com.project.Literalura.principal;

import java.util.Scanner;

public class Principal {
    private final Scanner read = new Scanner(System.in);

    private final String endereco = "https://gutendex.com/books";


    public void exibeMenu() {
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
        var option = read.nextInt();
        read.nextLine();


        switch(option) {
            case 1:

        }

        var json = consumo.obterDados(endereco);
        System.out.println(json);

    }
}
