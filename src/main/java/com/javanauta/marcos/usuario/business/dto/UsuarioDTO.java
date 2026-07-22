package com.javanauta.marcos.usuario.business.dto;

import com.javanauta.marcos.usuario.infrastructure.entity.Endereco;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;
}
