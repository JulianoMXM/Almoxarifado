package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Resistor;

@Repository
public interface ResistorRepository extends JpaRepository<Resistor, Long>{}
