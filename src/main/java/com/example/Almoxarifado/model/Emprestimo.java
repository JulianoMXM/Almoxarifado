package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.enums.StatusEmprestimoEnum;
import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    //@NotBlank(message = "O id do item emprestado é obrigatório")
    //private Long id;

    @NotBlank(message = "O SIAPE ou RA é obrigatório.")
    @Size(min = 7, max = 8, message = "O SIAPE ou RA deve conter entre 7 e 8 digitos.")
    private Integer solicitante;

    //@NotBlank(message = "O identificador do usuário responsável é obrigatório")
    //private Long id;

    @NotBlank(message = "A data de retirada é obrigatório.")
    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}", message = "A data deve estar no formato XX/XX/XX")
    private String dataRetirada;

    @NotBlank(message = "A data limite é obrigatório.")
    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}", message = "A data deve estar no formato XX/XX/XX")
    private String dataLimite;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}", message = "A data deve estar no formato XX/XX/XX")
    private String dataDevolucao;

    @NotBlank(message = "O status é obrigatório.")
    @IsEnum(enumClass = StatusEmprestimoEnum.class, message = "Status inválido. Use ATIVO, ATRASADO ou FINALIZADO.")
    private String status;
}
