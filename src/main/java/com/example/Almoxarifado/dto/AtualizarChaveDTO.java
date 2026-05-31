package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.StatusChaveEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarChaveDTO {

    private String sala;

    @IsEnum(enumClass = StatusChaveEnum.class, message = "Status inválido. Use DISPONÍVEL, EMPRESTADA ou DESATIVADA.")
    private String status;
}