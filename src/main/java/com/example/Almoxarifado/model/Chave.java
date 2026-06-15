package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.StatusChaveEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chaves")
@Getter
@Setter
public class Chave extends ItemEmprestavel{
    @NotBlank(message = "O identificador da sala é obrigatório.")
    private String sala;

    @NotBlank(message = "O status da chave é obrigatório.")
    @IsEnum(enumClass = StatusChaveEnum.class, message = "Status inválido. Use DISPONÍVEL, EMPRESTADA ou DESATIVADA.")
    private String status;
}
