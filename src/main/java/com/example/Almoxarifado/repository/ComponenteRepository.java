package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Componente;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long>{}
