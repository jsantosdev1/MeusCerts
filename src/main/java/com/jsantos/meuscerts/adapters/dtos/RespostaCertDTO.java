package com.jsantos.meuscerts.adapters.dtos;

import java.util.UUID;

public record RespostaCertDTO(
        UUID id,
        String titulo,
        String urlDownload,
        String status
) {
    public static RespostaCertDTO deDominio(com.jsantos.meuscerts.core.entities.Certificado c) {
        return new RespostaCertDTO(
                c.getId(),
                c.getTitulo(),
                c.getUrlArquivo(),
                "CRIADO_COM_SUCESSO"
        );
    }
}
