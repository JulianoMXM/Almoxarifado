package com.example.Almoxarifado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Indutor;

@Repository
public interface IndutorRepository extends JpaRepository<Indutor, Long>{
    List<Indutor> findByIndutanciaBetweenAndUnidadeDeMedida(Double min, Double max, String unidade);
    List<Indutor> findByIndutanciaGreaterThanEqualAndUnidadeDeMedida(Double min, String unidade);
    List<Indutor> findByIndutanciaLessThanEqualAndUnidadeDeMedida(Double max, String unidade);
    List<Indutor> findByUnidadeDeMedida(String unidade);
}
