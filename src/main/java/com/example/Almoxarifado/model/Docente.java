package com.example.Almoxarifado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "docentes")
@Getter
@Setter
public class Docente extends Pessoa {
    @NotBlank(message = "O SIAPE é obrigatório.")
    @Column(unique = true, nullable = false)
    @Size(min = 7, max = 8, message = "O SIAPE deve conter entre 7 e 8 digitos.")
    private String siape;
}
