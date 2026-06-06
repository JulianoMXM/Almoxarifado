package com.example.Almoxarifado.repository;

import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Docente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findBySiape(String siape);
}
