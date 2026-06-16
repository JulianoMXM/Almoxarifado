package com.example.Almoxarifado.security;

import com.example.Almoxarifado.common.enums.TipoUsuarioEnum;

public record CadastroDTO(String email, String senha, String nome, TipoUsuarioEnum tipoUsuario, String cpf) {
    
}
