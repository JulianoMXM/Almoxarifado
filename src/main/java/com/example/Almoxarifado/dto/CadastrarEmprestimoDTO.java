package com.example.Almoxarifado.dto;

import java.time.LocalDate;

import com.example.Almoxarifado.model.ItemEmprestavel;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastrarEmprestimoDTO {
    @NotNull(message = "O item emprestado é obrigatório.")
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "itemEmprestado_id", nullable = false)
    private ItemEmprestavel itemEmprestado;

    @NotNull(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpfSolicitante;

    @NotNull(message = "A quantidade emprestada é obrigatória.")
    @Min(value = 1, message = "A quantidade emprestada deve ser de pelo menos 1 item.")
    private Integer quantidade;

    @NotNull(message = "A data de retirada é obrigatória.")
    @PastOrPresent(message = "A data de retirada não pode ser uma data futura.")
    @JsonFormat(pattern = "dd/MM/yy")
    private LocalDate dataRetirada;

    @NotNull(message = "A data limite é obrigatória.")
    @Future(message = "A data de devolução não pode ser no passado.")
    @JsonFormat(pattern = "dd/MM/yy")
    private LocalDate dataLimite;
}
