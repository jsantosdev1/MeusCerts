package com.jsantos.meuscerts.adapters.gateways;

import com.jsantos.meuscerts.core.usecases.ports.ArmazenamentoArquivos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
public class CloudflareR2Gateway implements ArmazenamentoArquivos {

    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.public-domain}")
    private String publicDomain;

    public CloudflareR2Gateway(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String salvar(String nomeArquivo, InputStream conteudo, String tipoConteudo) {
        String nomeUnico = UUID.randomUUID() + "-" + nomeArquivo;

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nomeUnico)
                    .contentType(tipoConteudo)
                    .build();

            byte[] bytes = conteudo.readAllBytes();

            s3Client.putObject(putOb, RequestBody.fromBytes(bytes));

            return publicDomain + "/" + nomeUnico;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para o R2: " + e.getMessage(), e);
        }
    }
}
