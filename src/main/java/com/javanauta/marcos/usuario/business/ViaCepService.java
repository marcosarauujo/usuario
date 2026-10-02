package com.javanauta.marcos.usuario.business;

import com.javanauta.marcos.usuario.infrastructure.client.ViaCepClient;
import com.javanauta.marcos.usuario.infrastructure.client.ViaCepDTO;
import com.javanauta.marcos.usuario.infrastructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscarDadosDeEndereco(String cep) {
        return client.buscaDadosDeEndereco(processarCep(cep));

    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");
        if (!cepFormatado.matches("^[0-9]{8}$")) {
            throw new com.javanauta.marcos.usuario.infrastructure.exceptions.IllegalArgumentException("O Cep contém caracteres inválidos, favor verificar");
        }
        return cepFormatado;
    }
}
