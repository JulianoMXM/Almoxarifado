package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.TipoDiodoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "diodos")
@Getter
@Setter
public class Diodo extends Componente{
    @NotBlank(message = "A tensao reversa máxima é obrigatória.")
    @PositiveOrZero(message = "A tensão reversa máxima não pode ser negativa.")
    @Max(value = 1000, message = "A tensão reversa é muito alta.")
    private Float tensaoReversaMaxima;

    @NotBlank(message = "A corrente direta máxima é obrigatória.")
    @PositiveOrZero(message = "A corrente direta máxima não pode ser negativa.")
    @Max(value = 1000, message = "A corrente direta é muito alta.")
    private Float correnteDiretaMaxima;

    @NotBlank(message = "A queda de tensão é obrigatória.")
    @PositiveOrZero(message = "A queda de tensão não pode ser negativa.")
    @Max(value = 100, message = "A queda de tensão é muito alta.")
    private Float quedaDeTensao;

    @NotBlank(message = "O tipo de diodo é obrigatório.")
    @IsEnum(enumClass = TipoDiodoEnum.class, message = "Tipo inválido. Use RETIFICADOR, ZENER, LED, FOTODIODO, SCHOTTKY ou VARICAP.")
    private String tipoDiodo;
}
