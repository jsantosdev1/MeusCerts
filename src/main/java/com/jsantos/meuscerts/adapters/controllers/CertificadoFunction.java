package com.jsantos.meuscerts.adapters.controllers;

import com.jsantos.meuscerts.adapters.dtos.RequisicaoCertDTO;
import com.jsantos.meuscerts.adapters.dtos.RespostaCertDTO;
import com.jsantos.meuscerts.core.entities.Certificado;
import com.jsantos.meuscerts.core.usecases.CriarCertificado;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.function.Function;

@Configuration
public class CertificadoFunction {

    private final CriarCertificado criarCertificadoUseCase;

    public CertificadoFunction(CriarCertificado criarCertificadoUseCase) {
        this.criarCertificadoUseCase = criarCertificadoUseCase;
    }

    @Bean
    public Function<RequisicaoCertDTO, RespostaCertDTO> processarCertificado() {
        return request -> {
            byte[] arquivosBytes = Base64.getDecoder().decode(request.conteudoBase64());
            InputStream fluxoArquivo = new ByteArrayInputStream(arquivosBytes);

            LocalDate data = request.dataEmissao() != null
                    ? LocalDate.parse(request.dataEmissao())
                    : LocalDate.now();

            Certificado certificadoCriado = criarCertificadoUseCase.executar(
                    request.titulo(),
                    request.descricao(),
                    request.categoria(),
                    request.urlValidacao(),
                    data,
                    request.nomeArquivo(),
                    fluxoArquivo,
                    request.tipoConteudo()
            );

            return RespostaCertDTO.deDominio(certificadoCriado);
        };
    }
}
