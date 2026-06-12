package com.example.Almoxarifado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Capacitor;

@Repository
public interface CapacitorRepository extends JpaRepository<Capacitor, Long>{
    List<Capacitor> findByCapacitanciaBetweenAndUnidadeDeMedida(Double min, Double max, String unidade);
    List<Capacitor> findByCapacitanciaGreaterThanEqualAndUnidadeDeMedida(Double min, String unidade);
    List<Capacitor> findByCapacitanciaLessThanEqualAndUnidadeDeMedida(Double max, String unidade);
    List<Capacitor> findByUnidadeDeMedida(String unidade);
}
