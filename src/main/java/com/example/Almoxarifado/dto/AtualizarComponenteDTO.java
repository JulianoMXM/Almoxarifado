package com.example.Almoxarifado.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarComponenteDTO {
    @PositiveOrZero(message = "A quantidade de itens não pode ser negativa")
    @Max(value = 1000000, message = "A quantidade de itens é muito alta.")
    private int qntDisponivel;
    private String modelo;
    private String descricao;
}
