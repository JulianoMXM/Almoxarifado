package com.example.Almoxarifado.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "discentes")
@Getter
@Setter
public class Discente extends Pessoa {
    @NotBlank(message = "O RA é obrigatório.")
    @Size(min = 7, max = 7, message = "O RA deve conter 7 digitos.")
    @Column(unique = true, nullable = false)
    private String ra;
}
