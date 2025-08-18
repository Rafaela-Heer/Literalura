package com.project.literalura.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
