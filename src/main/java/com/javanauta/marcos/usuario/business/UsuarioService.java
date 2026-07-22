package com.javanauta.marcos.usuario.business;

import com.javanauta.marcos.usuario.business.converter.UsuarioConverter;
import com.javanauta.marcos.usuario.business.dto.UsuarioDTO;
import com.javanauta.marcos.usuario.infrastructure.entity.Usuario;
import com.javanauta.marcos.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

}
