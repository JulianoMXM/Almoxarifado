package com.example.Almoxarifado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "componentes")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Componente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A quantidade de itens é obrigatória.")
    @PositiveOrZero(message = "A quantidade de itens não pode ser negativa")
    @Max(value = 1000000, message = "A quantidade de itens é muito alta.")
    private int qntDisponivel;

    @NotBlank(message = "O modelo é obrigatório.")
    private String modelo;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;
}
