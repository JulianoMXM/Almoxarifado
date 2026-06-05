package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.TipoDiodoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarDiodoDTO {
    @PositiveOrZero(message = "A tensão reversa máxima não pode ser negativa.")
    private Float tensaoReversaMaxima;

    @PositiveOrZero(message = "A corrente direta máxima não pode ser negativa.")
    private Float correnteDiretaMaxima;

    @PositiveOrZero(message = "A queda de tensão não pode ser negativa.")
    private Float tolerancia;

    @IsEnum(enumClass = TipoDiodoEnum.class, message = "Tipo inválido. Use RETIFICADOR, ZENER, LED, FOTODIODO, SCHOTTKY ou VARICAP.")
    private String tipoDiodo;
}
