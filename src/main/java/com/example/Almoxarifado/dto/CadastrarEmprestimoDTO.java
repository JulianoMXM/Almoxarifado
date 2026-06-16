package com.example.Almoxarifado.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private Long idItem;

    @NotNull(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpfSolicitante;

    @NotNull(message = "O funcionário precisa estar logado.")
    private Long idFuncionario;

    @NotNull(message = "A quantidade emprestada é obrigatória.")
    @Min(value = 1, message = "A quantidade emprestada deve ser de pelo menos 1 item.")
    private Integer quantidade;

    @NotNull(message = "A data de retirada é obrigatória.")
    @PastOrPresent(message = "A data de retirada não pode ser uma data futura.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataRetirada;

    @NotNull(message = "A data limite é obrigatória.")
    @Future(message = "A data de devolução não pode ser no passado.")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataLimite;
}
