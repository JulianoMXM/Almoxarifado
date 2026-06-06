package com.example.Almoxarifado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Discente;

@Repository
public interface DiscenteRepository extends JpaRepository<Discente, Long> {
    Optional<Discente> findByRa(String ra);
}
