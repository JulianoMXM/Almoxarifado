package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarIndutorDTO extends AtualizarComponenteDTO{
    @PositiveOrZero(message = "A indutância não pode ser negativa.")
    @Max(value = 100000000, message = "A indutância é muito alta.")
    private Double indutancia;

    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    @Max(value = 1000, message = "A corrente máxima é muito alta.")
    private Float correnteMaxima;

    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    @Max(value = 100, message = "A tolerância é muito alta.")
    private Float tolerancia;

    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
