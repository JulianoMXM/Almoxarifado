package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "indutores")
@Getter
@Setter
public class Indutor extends Componente{
    @NotBlank(message = "A indutância é obrigatória.")
    @PositiveOrZero(message = "A indutância não pode ser negativa.")
    private Float indutancia;

    @NotBlank(message = "A corrente máxima é obrigatória.")
    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    private Float correnteMaxima;

    @NotBlank(message = "A tolerância é obrigatória.")
    @PositiveOrZero(message = "A tolerância não pode ser negativa.")
    private Float tolerancia;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    @IsEnum(enumClass = UnidadeDeMedidaEnum.class, message = "Unidade inválida. Use p, u, m, padrao, k ou M.")
    private String unidadeDeMedida;
}
