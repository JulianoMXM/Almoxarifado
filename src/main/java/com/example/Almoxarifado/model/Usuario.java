package com.example.Almoxarifado.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario extends Pessoa {
    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    private Boolean adm;
    // Ainda sem ideia de como fazer essa parte de adm, deixar o boolean aqui e dps ver como fazer essa lógica
}
