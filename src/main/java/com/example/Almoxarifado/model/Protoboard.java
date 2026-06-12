package com.example.Almoxarifado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "protoboards")
@Getter
@Setter
public class Protoboard extends Componente{
    @NotNull(message = "A potência máxima é obrigatória.")
    @PositiveOrZero(message = "A potência máxima não pode ser negativa.")
    @Max(value = 1000, message = "A potência máxima é muito alta.")
    private Float potenciaMaxima;

    @NotNull(message = "A corrente máxima é obrigatória.")
    @PositiveOrZero(message = "A corrente máxima não pode ser negativa.")
    @Max(value = 1000, message = "A corrente máxima é muito alta.")
    private Float correnteMaxima;

    @NotNull(message = "A tensão máxima é obrigatória.")
    @PositiveOrZero(message = "A tensão máxima não pode ser negativa.")
    @Max(value = 1000, message = "A tensão máxima é muito alta.")
    private Float tensaoMaxima;
}
