package com.jsantos.meuscerts.core.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Certificado {

    private final UUID id;
    private final String titulo;
    private final String descricao;
    private final String categoria;
    private final String urlArquivo;
    private final String urlValidacao;
    private final LocalDate dataEmissao;
    private final boolean visivel;

    public Certificado(String titulo,
                       String descricao,
                       String categoria,
                       String urlArquivo,
                       String urlValidacao,
                       LocalDate dataEmissao)
    {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.urlArquivo = urlArquivo;
        this.urlValidacao = urlValidacao;
        this.dataEmissao = dataEmissao;
        this.visivel = true;

        validar();
    }

    public Certificado(UUID id,
                       String titulo,
                       String descricao,
                       String categoria,
                       String urlArquivo,
                       String urlValidacao,
                       LocalDate dataEmissao,
                       boolean visivel) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.urlArquivo = urlArquivo;
        this.urlValidacao = urlValidacao;
        this.dataEmissao = dataEmissao;
        this.visivel = visivel;
    }

    private void validar() {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do certificado é obrigatório");
        }
        if (urlArquivo == null || urlArquivo.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL do arquivo é obrigatória");
        }
        if (dataEmissao != null && dataEmissao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de emissão não pode ser futura");
        }
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getUrlArquivo() { return urlArquivo; }
    public String getUrlValidacao() { return urlValidacao; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public boolean isVisivel() { return visivel; }
}
