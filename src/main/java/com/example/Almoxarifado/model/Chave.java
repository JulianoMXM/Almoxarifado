package com.example.Almoxarifado.model;

import com.example.Almoxarifado.common.StatusEnum;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Chave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sala;
    private StatusEnum status;
}
