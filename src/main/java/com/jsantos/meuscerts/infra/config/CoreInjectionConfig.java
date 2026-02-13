package com.jsantos.meuscerts.infra.config;

import com.jsantos.meuscerts.core.usecases.CriarCertificado;
import com.jsantos.meuscerts.core.usecases.ports.ArmazenamentoArquivos;
import com.jsantos.meuscerts.core.usecases.ports.RepositorioCertificado;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreInjectionConfig {
    @Bean
    public CriarCertificado criarCertificado(
            RepositorioCertificado repositorio,
            ArmazenamentoArquivos armazenamento) {
        return new CriarCertificado(repositorio, armazenamento);
    }
}
