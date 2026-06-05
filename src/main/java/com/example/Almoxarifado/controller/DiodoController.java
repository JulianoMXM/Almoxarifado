package com.example.Almoxarifado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Almoxarifado.dto.AtualizarDiodoDTO;
import com.example.Almoxarifado.model.Diodo;
import com.example.Almoxarifado.repository.DiodoRepository;

import jakarta.validation.Valid;

public class DiodoController {
    @Autowired
    private DiodoRepository repository;

    @PostMapping
    public Diodo cadastrarDiodo(@Valid @RequestBody Diodo novoDiodo){
        return repository.save(novoDiodo);
    }

    @PatchMapping("/{id}")
    public Diodo atualziarDiodo(@Valid @RequestBody AtualizarDiodoDTO dto, @PathVariable Long id) throws NotFoundException{
        Diodo diodoExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getTensaoReversaMaxima() != null){
            diodoExistente.setTensaoReversaMaxima(dto.getTensaoReversaMaxima());
        }
        if(dto.getCorrenteDiretaMaxima() != null){
            diodoExistente.setCorrenteDiretaMaxima(dto.getCorrenteDiretaMaxima());
        }
        if(dto.getTolerancia() != null){
            diodoExistente.setTolerancia(dto.getTolerancia());
        }
        if(dto.getTipoDiodo() != null){
            diodoExistente.setTipoDiodo(dto.getTipoDiodo());
        }
        return repository.save(diodoExistente);
    }
}
