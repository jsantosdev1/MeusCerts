package com.jsantos.meuscerts.core.usecases.ports;

import java.io.InputStream;

public interface ArmazenamentoArquivos {
    /**
     * Faz o upload do arquivo e retorna a URL pública de acesso.
     * @param nomeArquivo ex: "certificado-java-2026.pdf"
     * @param conteudo O fluxo de bytes do arquivo
     * @param tipoConteudo ex: "application/pdf" ou "image/png"
     * @return URL pública)
     */
    String salvar(String nomeArquivo, InputStream conteudo, String tipoConteudo);
}
