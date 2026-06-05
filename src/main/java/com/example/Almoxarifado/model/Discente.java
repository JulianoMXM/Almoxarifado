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
    @Size(min = 8, max = 8, message = "O RA deve conter 8 digitos.")
    // 8 digitos no ra pq estou levando em consideração que o sitema é para o nosso campus q o ra é 8 digitos.
    private Integer ra;
}
