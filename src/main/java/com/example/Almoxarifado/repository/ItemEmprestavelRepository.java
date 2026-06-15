package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.ItemEmprestavel;

@Repository
public interface ItemEmprestavelRepository extends JpaRepository<ItemEmprestavel, Long>{
    
}
