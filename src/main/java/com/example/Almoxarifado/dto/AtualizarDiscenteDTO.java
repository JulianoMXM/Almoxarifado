package com.example.Almoxarifado.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarDiscenteDTO extends AtualizarPessoaDTO {
    @Size(min = 8, max = 8, message = "O RA deve conter 8 digitos.")
    private Integer ra;
}
