package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Chave;

@Repository
public interface ChaveRepository extends JpaRepository<Chave, Long> {

}
