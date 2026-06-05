package com.example.Almoxarifado.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarProtoboardDTO extends AtualizarComponenteDTO{
    @PositiveOrZero(message = "A potência máxima não pode ser negativa.")
    @Max(value = 1000, message = "A potência máxima é muito alta.")
    private Float potenciaMaxima;

    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    @Max(value = 1000, message = "A corrente máxima é muito alta.")
    private Float correnteMaxima;

    @PositiveOrZero(message = "A tensão máxima não pode ser negativa.")
    @Max(value = 1000, message = "A tensão máxima é muito alta.")
    private Float tensaoMaxima;
}
