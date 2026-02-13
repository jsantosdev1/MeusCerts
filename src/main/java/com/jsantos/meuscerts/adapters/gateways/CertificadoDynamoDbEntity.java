package com.jsantos.meuscerts.adapters.gateways;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class CertificadoDynamoDbEntity {

    private String id;
    private String titulo;
    private String descricao;
    private String categoria;
    private String urlArquivo;
    private String urlValidacao;
    private String dataEmissao;

    public CertificadoDynamoDbEntity() {}

    @DynamoDbPartitionKey // Nossa Chave Primária (PK)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getUrlArquivo() { return urlArquivo; }
    public void setUrlArquivo(String urlArquivo) { this.urlArquivo = urlArquivo; }

    public String getUrlValidacao() { return urlValidacao; }
    public void setUrlValidacao(String urlValidacao) { this.urlValidacao = urlValidacao; }

    public String getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(String dataEmissao) { this.dataEmissao = dataEmissao; }
}
