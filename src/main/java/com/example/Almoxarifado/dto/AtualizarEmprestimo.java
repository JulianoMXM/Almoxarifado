package com.example.Almoxarifado.dto;

import com.example.Almoxarifado.common.enums.StatusEmprestimoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AtualizarEmprestimo{

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}", message = "A data deve estar no formato XX/XX/XX")
    private String dataDevolucao;

    @IsEnum(enumClass = StatusEmprestimoEnum.class, message = "Status inválido. Use ATIVO, ATRASADO ou FINALIZADO.")
    private String status;
}