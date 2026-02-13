package com.jsantos.meuscerts.adapters.dtos;

public record RequisicaoCertDTO(String titulo,
                                String descricao,
                                String categoria,
                                String urlValidacao,
                                String dataEmissao,
                                String nomeArquivo,
                                String tipoConteudo,
                                String conteudoBase64
)
{
}
