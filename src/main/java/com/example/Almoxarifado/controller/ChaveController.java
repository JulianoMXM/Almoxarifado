package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.Chave;
import com.example.Almoxarifado.repository.ChaveRepository;

@RestController
@RequestMapping("/chaves")
public class ChaveController {
    
    @Autowired
    private ChaveRepository repository;

    @GetMapping
    public List<Chave> listarTodos() {
        return repository.findAll();
    }
}
