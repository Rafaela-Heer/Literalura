<h1 align="center">📖Literalura📖</h1>
<h2 align="center">Catálogo de Livros (Spring + JPA + PostgreSQL)</h2>
Ferramenta em java que busca livros na Gutendex API (Project Gutenberg), salva no PostgreSQL e permite listar/consultar por título/autor, idioma, ano de vida de autores e top 10 downloads.

## Funcionalidades
- Buscar e importar livros por título ou autor `?search=`, opcionalmente filtrando por idiomas `?languages=`;
- Listar livros salvos.
- Listar autores salvos (ordenados);
- Listar autores vivos em determinado ano;
- Listar livros por idioma + quantidade total;
- Ver Top 10 livros por número de downloads;
- Buscar livros por autor já registrado no banco.

## Tecnologias
- Java 21, Spring Boot 3, Spring Data JPA;
- PostgreSQL 15+ (porta padrão 5432)
- Jackson (JSON), HttpClient (Java 11+)

## Estrutura de pacotes - resumo
```
com.project.literalura
├── LiteraluraApplication.java            # entrypoint (CommandLineRunner)
├── principal/Principal.java              # menu / CLI
├── model/
│   ├── dto/                              # mapeamento JSON da Gutendex
│   │   ├── GutendexResponse.java
│   │   ├── GutenBook.java
│   │   └── GutenAuthor.java
│   └── entity/                           # entidades JPA
│       ├── Book.java
│       └── Author.java
├── repository/
│   ├── BookRepository.java
│   └── AuthorRepository.java
└── service/
    ├── BookCatalogService.java           # busca API + persistência
    ├── ConsumoApi.java                   # HTTP client
    ├── ConverteDados.java                # Jackson ObjectMapper
    └── IConverteDados.java
```
## Pré-requisitos
- Java 21
- PostgreSQL rodando e banco criado (`CREATE DATABASE literalura;`)
- Usuário com acesso (ex: `postgres`)

## Recomendado:
### Ajuste de colunas longas
No pgAdmin ou `psql`:
```sql
ALTER TABLE public.books   ALTER COLUMN title TYPE text;
ALTER TABLE public.authors ALTER COLUMN name  TYPE text;
```
O `ddl-auto=update` pode não alterar tipo existente; execute o `ALTER`.

## Como rodar
```bash
# Maven
./mvnw spring-boot:run
# ou
mvn clean package
java -jar target/literalura-*.jar
```
Ao iniciar, você verá o menu:
```mardown
============================
      CATÁLOGO
============================

1 - Buscar livro pelo título/autor (com filtro opcional de idioma)
2 - Listar livros
3 - Lista autores
4 - Listar autores em determinado ano
5 - Listar livros por idioma (mostra total)
6 - Top 10 livros por downloads
7 - Buscar livros por autor registrado

0 - Sair

 ->
```
### IMPORTANTE
a Gutendex/PG tem apenas obras de domínio público.
Termos como “Harry Potter” não retornam resultados.

## Desenvolvedora:
 ### Rafaela Heer Robinson (rafaelaheerr@hotmail.com)
 
