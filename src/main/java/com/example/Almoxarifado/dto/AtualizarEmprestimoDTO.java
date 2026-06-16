package com.example.Almoxarifado.dto;

import java.time.LocalDate;

import com.example.Almoxarifado.common.enums.StatusEmprestimoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarEmprestimoDTO{
    @PastOrPresent(message = "A data de devolução não pode ser no futuro.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDevolucao;

    @IsEnum(enumClass = StatusEmprestimoEnum.class, message = "Status inválido. Use ATIVO, ATRASADO ou FINALIZADO.")
    private String status;
}