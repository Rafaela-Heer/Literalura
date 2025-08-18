package com.project.literalura.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ConsumoApi {

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public String obterDados(String endereco) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .header("User-Agent", "Literalura/1.0")
                    .GET()
                    .build();

            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());

            int sc = resp.statusCode();
            if (sc != 200) {
                throw new RuntimeException("Chamada HTTP falhou: status=" + sc + " url=" + endereco);
            }

            String body = resp.body();
            if (body == null || body.isBlank()) {
                throw new RuntimeException("Resposta vazia da API: " + endereco);
            }
            return body;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao chamar API: " + endereco, e);
        }
    }
}
