package com.example.Almoxarifado.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarProtoboardDTO {
    @PositiveOrZero(message = "A potência máxima não pode ser negativa.")
    private Float potenciaMaxima;

    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    private Float correnteMaxima;

    @PositiveOrZero(message = "A tensão máxima não pode ser negativa.")
    private Float tensaoMaxima;
}
