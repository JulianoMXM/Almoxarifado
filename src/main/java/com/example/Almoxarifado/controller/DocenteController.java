package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.Docente;
import com.example.Almoxarifado.repository.DocenteRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequestMapping("/docente")
public class DocenteController {
    @Autowired
    private DocenteRepository repository;

    @GetMapping
    public List<Docente> consultarTodosDocentes(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Docente consultarDocente(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }
    
    @GetMapping("Docente/{siape}") // aqui vai encontrar o docente usando o siape, espero que funcione xd
    public Docente consultarDocenteSiape(@PathVariable String siape) throws NotFoundException {
        return repository.findBySiape(siape).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Docente cadastrarDocente(@RequestBody Docente novoDocente){
        return repository.save(novoDocente);
    }
    
}
