package com.javanauta.marcos.usuario.business;

import com.javanauta.marcos.usuario.business.converter.UsuarioConverter;
import com.javanauta.marcos.usuario.business.dto.UsuarioDTO;
import com.javanauta.marcos.usuario.infrastructure.entity.Usuario;
import com.javanauta.marcos.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.marcos.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.marcos.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("email já cadastrado" + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("email já cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Email não encontrado" + email)
        );
    }

    public void deletaUsuarioPorEmail(String email) {

        usuarioRepository.deleteByEmail(email);
    }


}
