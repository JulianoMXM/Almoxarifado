package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Diodo;

@Repository
public interface DiodoRepository extends JpaRepository<Diodo, Long>{}
