package com.example.Almoxarifado.model;

import java.time.LocalDate;

import com.example.Almoxarifado.common.enums.StatusEmprestimoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O item emprestado é obrigatório.")
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "itemEmprestado_id", nullable = false)
    private ItemEmprestavel itemEmprestado;

    @NotNull(message = "O solicitante é obrigatório.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Pessoa solicitante;

    @NotNull(message = "O funcionário é obrigatório.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Pessoa funcionario;

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

    @Future(message = "A data de devolução não pode ser no passado.")
    @JsonFormat(pattern = "dd/MM/yy")
    private LocalDate dataDevolucao;

    @NotBlank(message = "O status é obrigatório.")
    @IsEnum(enumClass = StatusEmprestimoEnum.class, message = "Status inválido. Use ATIVO, ATRASADO ou FINALIZADO.")
    private String status;
}
