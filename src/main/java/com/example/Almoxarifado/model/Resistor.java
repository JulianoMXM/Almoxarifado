package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resistores")
@Getter
@Setter
public class Resistor extends Componente{
    @NotBlank(message = "A resistência é obrigatória.")
    @PositiveOrZero(message = "A resistência não pode ser negativa.")
    private Float resistencia;

    @NotBlank(message = "A potência máxima é obrigatória.")
    @PositiveOrZero(message = "A potência máxima não pode ser negativa.")
    private Float potenciaMaxima;

    @NotBlank(message = "A tolerância é obrigatória.")
    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    private Float tolerancia;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
