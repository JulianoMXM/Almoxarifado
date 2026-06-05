package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.TipoDiodoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarDiodoDTO {
    @PositiveOrZero(message = "A tensão reversa máxima não pode ser negativa.")
    @Max(value = 1000, message = "A tensão reversa é muito alta.")
    private Float tensaoReversaMaxima;

    @PositiveOrZero(message = "A corrente direta máxima não pode ser negativa.")
    @Max(value = 1000, message = "A corrente direta é muito alta.")
    private Float correnteDiretaMaxima;

    @PositiveOrZero(message = "A queda de tensão não pode ser negativa.")
    @Max(value = 100, message = "A queda de tensão é muito alta.")
    private Float quedaDeTensao;

    @IsEnum(enumClass = TipoDiodoEnum.class, message = "Tipo inválido. Use RETIFICADOR, ZENER, LED, FOTODIODO, SCHOTTKY ou VARICAP.")
    private String tipoDiodo;
}
