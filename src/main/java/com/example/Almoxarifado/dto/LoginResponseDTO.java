package com.example.Almoxarifado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private String nome;
    private String tipoUsuario;

    public LoginResponseDTO(String token, Long userId, String email, String nome, String tipoUsuario) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }
}
