package com.example.Almoxarifado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Almoxarifado.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
