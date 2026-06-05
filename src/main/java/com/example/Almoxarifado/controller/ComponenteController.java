package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.Componente;
import com.example.Almoxarifado.repository.ComponenteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/componente")
public class ComponenteController {

    @Autowired
    private ComponenteRepository repository;

    @GetMapping
    public List<Componente> consultarTodosComponentes(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Componente consultarComponente(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @DeleteMapping("/{id}")
    public String deletarComponente(@PathVariable Long id) throws NotFoundException{
        Componente componenteExistente = this.consultarComponente(id);
        repository.delete(componenteExistente);
        return "Componente deletado com sucesso.";
    }
}
