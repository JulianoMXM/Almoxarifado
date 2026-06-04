package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Indutor;

@Repository
public interface IndutorRepository extends JpaRepository<Indutor, Long>{}
