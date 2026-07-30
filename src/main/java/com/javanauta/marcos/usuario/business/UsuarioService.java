package com.javanauta.marcos.usuario.business;

import com.javanauta.marcos.usuario.business.converter.UsuarioConverter;
import com.javanauta.marcos.usuario.business.dto.EnderecoDTO;
import com.javanauta.marcos.usuario.business.dto.TelefoneDTO;
import com.javanauta.marcos.usuario.business.dto.UsuarioDTO;
import com.javanauta.marcos.usuario.infrastructure.entity.Endereco;
import com.javanauta.marcos.usuario.infrastructure.entity.Telefone;
import com.javanauta.marcos.usuario.infrastructure.entity.Usuario;
import com.javanauta.marcos.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.marcos.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.marcos.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.marcos.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.marcos.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.marcos.usuario.infrastructure.security.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final TelefoneRepository telefoneRepository;
    private final EnderecoRepository enderecoRepository;

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

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Email não encontrado" + email))
            );
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {

        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(UsuarioDTO dto, String token) {
        //procurou uemail do usuario atéves do Token
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //buscou dados de usaurio no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado"));

        //mesclou os dados que recebemos na requisicao DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        //salvou os dados do usuario convertido e depois pegou pegou o retorno e converteu para usuariodto
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
    public EnderecoDTO atualizaEndereco (Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(()->
                new ResourceNotFoundException("id não encontrado"));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }
    public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO telefonDTO){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(()->
                new ResourceNotFoundException("id não encontrado"));
        Telefone telefone = usuarioConverter.updateTelefone(telefonDTO, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}
