package com.example.Almoxarifado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.LoginDTO;
import com.example.Almoxarifado.dto.LoginResponseDTO;
import com.example.Almoxarifado.model.Usuario;
import com.example.Almoxarifado.repository.UsuarioRepository;
import com.example.Almoxarifado.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            // Buscar usuário por email
            Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new NotFoundException());

            // Validar senha
            if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
                log.warn("Tentativa de login com senha inválida para: {}", loginDTO.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
            }

            // Gerar token JWT
            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getId());

            log.info("Login bem-sucedido para: {}", loginDTO.getEmail());
            return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getId(), usuario.getEmail(), usuario.getNome(), usuario.getTipoUsuario().toString()));

        } catch (NotFoundException e) {
            log.warn("Tentativa de login com email inexistente: {}", loginDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
        } catch (Exception e) {
            log.error("Erro durante login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}
