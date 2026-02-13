package com.jsantos.meuscerts.core.usecases;

import com.jsantos.meuscerts.core.entities.Certificado;
import com.jsantos.meuscerts.core.usecases.ports.ArmazenamentoArquivos;
import com.jsantos.meuscerts.core.usecases.ports.RepositorioCertificado;

import java.io.InputStream;
import java.time.LocalDate;

public class CriarCertificado {
    private final RepositorioCertificado repositorio;
    private final ArmazenamentoArquivos armazenamento;

    public CriarCertificado(RepositorioCertificado repositorio, ArmazenamentoArquivos armazenamento) {
        this.repositorio = repositorio;
        this.armazenamento = armazenamento;
    }

    public Certificado executar(String titulo,
                                String descricao,
                                String categoria,
                                String urlValidacao,
                                LocalDate dataEmissao,
                                String nomeArquivo,
                                InputStream conteudoArquivo,
                                String tipoConteudo) {

        String urlPublica = armazenamento.salvar(nomeArquivo, conteudoArquivo, tipoConteudo);

        Certificado novoCertificado = new Certificado(
                titulo,
                descricao,
                categoria,
                urlPublica,
                urlValidacao,
                dataEmissao
        );

        repositorio.salvar(novoCertificado);

        return novoCertificado;
    }
}
