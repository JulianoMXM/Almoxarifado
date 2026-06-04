package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Capacitor;

@Repository
public interface CapacitorRepository extends JpaRepository<Capacitor, Long>{}
