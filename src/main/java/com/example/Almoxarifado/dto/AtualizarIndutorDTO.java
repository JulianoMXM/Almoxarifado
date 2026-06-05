package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarIndutorDTO {
    @PositiveOrZero(message = "A indutância não pode ser negativa.")
    private Float indutancia;

    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    private Float correnteMaxima;

    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    private Float tolerancia;

    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
