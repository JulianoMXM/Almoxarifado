package com.example.Almoxarifado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.model.Capacitor;

@Repository
public interface CapacitorRepository extends JpaRepository<Capacitor, Long>{
    List<Capacitor> findByCapacitanciaBetweenAndUnidadeDeMedida(Double min, Double max, UnidadeDeMedidaEnum unidade);
    List<Capacitor> findByCapacitanciaGreaterThanEqualAndUnidadeDeMedida(Double min, UnidadeDeMedidaEnum unidade);
    List<Capacitor> findByCapacitanciaLessThanEqualAndUnidadeDeMedida(Double max, UnidadeDeMedidaEnum unidade);
    List<Capacitor> findByUnidadeDeMedida(UnidadeDeMedidaEnum unidade);
}
