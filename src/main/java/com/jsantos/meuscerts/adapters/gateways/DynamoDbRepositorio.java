package com.jsantos.meuscerts.adapters.gateways;

import com.jsantos.meuscerts.core.entities.Certificado;
import com.jsantos.meuscerts.core.usecases.ports.RepositorioCertificado;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DynamoDbRepositorio implements RepositorioCertificado {

    private final DynamoDbTable<CertificadoDynamoDbEntity> tabela;

    public DynamoDbRepositorio(DynamoDbEnhancedClient client) {
        this.tabela = client.table("meuscerts-${var.env}", TableSchema.fromBean(CertificadoDynamoDbEntity.class));
    }

    @Override
    public void salvar(Certificado certificado) {
        CertificadoDynamoDbEntity entity = new CertificadoDynamoDbEntity();
        entity.setId(certificado.getId().toString());
        entity.setTitulo(certificado.getTitulo());
        entity.setDescricao(certificado.getDescricao());
        entity.setCategoria(certificado.getCategoria());
        entity.setUrlArquivo(certificado.getUrlArquivo());
        entity.setUrlValidacao(certificado.getUrlValidacao());
        if (certificado.getDataEmissao() != null) {
            entity.setDataEmissao(certificado.getDataEmissao().toString());
        }

        tabela.putItem(entity);
    }

    @Override
    public List<Certificado> listarTodos() {
        return tabela.scan().items().stream()
                .map(this::paraDominio)
                .collect(Collectors.toList());
    }

    private Certificado paraDominio(CertificadoDynamoDbEntity entity) {
        return new Certificado(
                UUID.fromString(entity.getId()),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getCategoria(),
                entity.getUrlArquivo(),
                entity.getUrlValidacao(),
                entity.getDataEmissao() != null ? LocalDate.parse(entity.getDataEmissao()) : null,
                true
        );
    }
}
