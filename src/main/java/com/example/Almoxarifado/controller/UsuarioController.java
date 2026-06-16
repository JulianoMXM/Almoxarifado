package com.example.Almoxarifado.controller;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Almoxarifado.common.enums.TipoUsuarioEnum;
import com.example.Almoxarifado.dto.AtualizarUsuarioDTO;
import com.example.Almoxarifado.model.Pessoa;
import com.example.Almoxarifado.model.Usuario;
import com.example.Almoxarifado.repository.PessoaRepository;
import com.example.Almoxarifado.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> consultarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario consultarUsuarioId(@PathVariable Long id) throws NotFoundException {
        return usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario novoUsuario) throws BadRequestException{
        Optional<Pessoa> cpfExistente = pessoaRepository.findByCpf(novoUsuario.getCpf());
        if(cpfExistente.isPresent()){
            throw new BadRequestException("CPF já cadastrado no sistema.");
        }
        Optional<Pessoa> emailExistente = pessoaRepository.findByEmail(novoUsuario.getEmail());
        if(emailExistente.isPresent()){
            throw new BadRequestException("Email já cadastrado no sistema.");
        }
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(novoUsuario);
    }

    // a logica terá que mudar dependendo de como funcionar o sistema de login, porém ja fiz algo q eu considero uma base
    @PatchMapping("/{id}") 
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Usuario atualizarUsuario(@Valid @RequestBody AtualizarUsuarioDTO dto, @PathVariable Long id) throws NotFoundException {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException());

        if (dto.getSenha() != null) {
            usuarioExistente.setSenha(dto.getSenha());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @PatchMapping("/promover-adm/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Usuario promoverUsuarioParaAdmin(@PathVariable Long id) throws NotFoundException {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException());
        usuarioExistente.setTipoUsuario(TipoUsuarioEnum.ADMINISTRADOR);
        return usuarioRepository.save(usuarioExistente);
    }
}
