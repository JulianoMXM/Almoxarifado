package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.AtualizarIndutorDTO;
import com.example.Almoxarifado.model.Indutor;
import com.example.Almoxarifado.repository.IndutorRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/indutor")
public class IndutorController {
    @Autowired
    private IndutorRepository repository;

    @GetMapping
    public List<Indutor> consultarTodosIndutores(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Indutor consultarIndutor(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Indutor cadastrarIndutor(@Valid @RequestBody Indutor novoIndutor){
        return repository.save(novoIndutor);
    }

    @PatchMapping("/{id}")
    public Indutor atualizarIndutor(@Valid @RequestBody AtualizarIndutorDTO dto, @PathVariable Long id) throws NotFoundException{
        Indutor indutorExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getIndutancia() != null){
            indutorExistente.setIndutancia(dto.getIndutancia());
        }
        if(dto.getCorrenteMaxima() != null){
            indutorExistente.setCorrenteMaxima(dto.getCorrenteMaxima());
        }
        if(dto.getTolerancia() != null){
            indutorExistente.setTolerancia(dto.getTolerancia());
        }
        if(dto.getUnidadeDeMedida() != null){
            indutorExistente.setUnidadeDeMedida(dto.getUnidadeDeMedida());
        }
        return repository.save(indutorExistente);
    }
}
