package com.example.Almoxarifado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Chave;

@Repository
public interface ChaveRepository extends JpaRepository<Chave, Long> {
    List<Chave> findBySala(String sala);
}
