package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.Almoxarifado.dto.AtualizarUsuarioDTO;
import com.example.Almoxarifado.model.Usuario;
import com.example.Almoxarifado.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public List<Usuario> consultarTodosUsuarios() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario consultarUsuarioId(@PathVariable Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario novoUsuario) {
        return repository.save(novoUsuario);
    }

    // a logica terá que mudar dependendo de como funcionar o sistema de login, porém ja fiz algo q eu considero uma base
    @PatchMapping("/{id}") 
    public Usuario atualizarUsuario(@Valid @RequestBody AtualizarUsuarioDTO dto, @PathVariable Long id, @RequestParam Long adminId) throws NotFoundException {
        verificarAdmin(adminId);

        Usuario usuarioExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if (dto.getSenha() != null) {
            usuarioExistente.setSenha(dto.getSenha());
        }
        if (dto.getAdm() != null) {
            usuarioExistente.setAdm(dto.getAdm());
        }

        return repository.save(usuarioExistente);
    }

    private void verificarAdmin(Long adminId) throws NotFoundException {
        Usuario admin = repository.findById(adminId).orElseThrow(() -> new NotFoundException());
        if (admin.getAdm() == null || !admin.getAdm()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas administradores podem atualizar usuários.");
        }
    }
}
