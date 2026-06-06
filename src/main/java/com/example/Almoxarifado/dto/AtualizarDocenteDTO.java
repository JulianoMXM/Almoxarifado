package com.example.Almoxarifado.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarDocenteDTO extends AtualizarPessoaDTO {
    @Size(min = 7, max = 8, message = "O SIAPE deve conter entre 7 e 8 digitos.")
    private Integer siape;
}
