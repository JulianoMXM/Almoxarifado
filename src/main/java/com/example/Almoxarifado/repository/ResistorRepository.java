package com.example.Almoxarifado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.model.Resistor;

@Repository
public interface ResistorRepository extends JpaRepository<Resistor, Long>{
    List<Resistor> findByResistenciaBetweenAndUnidadeDeMedida(Double min, Double max, UnidadeDeMedidaEnum unidade);
    List<Resistor> findByResistenciaGreaterThanEqualAndUnidadeDeMedida(Double min, UnidadeDeMedidaEnum unidade);
    List<Resistor> findByResistenciaLessThanEqualAndUnidadeDeMedida(Double max, UnidadeDeMedidaEnum unidade);
    List<Resistor> findByUnidadeDeMedida(UnidadeDeMedidaEnum unidade);
}
