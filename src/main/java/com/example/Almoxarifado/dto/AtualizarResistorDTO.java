package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarResistorDTO extends AtualizarComponenteDTO{
    @PositiveOrZero(message = "A resistência não pode ser negativa.")
    @Max(value = 100000000, message = "A resistência é muito alta.")
    private Double resistencia;

    @PositiveOrZero(message = "A potência máxima não pode ser negativa.")
    @Max(value = 1000, message = "A potência máxima é muito alta.")
    private Float potenciaMaxima;

    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    @Max(value = 100, message = "A tolerância é muito alta.")
    private Float tolerancia;

    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
