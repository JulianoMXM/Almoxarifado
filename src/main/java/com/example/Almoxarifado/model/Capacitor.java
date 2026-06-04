package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "capacitores")
@Getter
@Setter
public class Capacitor extends Componente{
    @NotBlank(message = "A capacitância é obrigatória.")
    @PositiveOrZero(message = "A capacitância não pode ser negativa.")
    private float capacitancia;

    @NotBlank(message = "A tensão máxima é obrigatória.")
    @PositiveOrZero(message = "A tensão máxima não pode ser negativa.")
    private float tensaoMaxima;

    @NotBlank(message = "A tolerância é obrigatória.")
    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    private float tolerancia;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
