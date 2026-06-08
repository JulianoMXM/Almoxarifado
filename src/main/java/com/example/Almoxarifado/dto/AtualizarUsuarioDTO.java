package com.example.Almoxarifado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarUsuarioDTO extends AtualizarPessoaDTO {
    private String senha;
}
