package com.example.Almoxarifado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Almoxarifado.dto.AtualizarCapacitorDTO;
import com.example.Almoxarifado.model.Capacitor;
import com.example.Almoxarifado.repository.CapacitorRepository;

import jakarta.validation.Valid;

public class CapacitorController {
    @Autowired
    private CapacitorRepository repository;

    @PostMapping
    public Capacitor cadastrarCapacitor(@Valid @RequestBody Capacitor novoCapacitor){
        return repository.save(novoCapacitor);
    }

    @PatchMapping("/{id}")
    public Capacitor atualizarCapacitor(@Valid @RequestBody AtualizarCapacitorDTO dto, @PathVariable Long id) throws NotFoundException{
        Capacitor capacitorExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getCapacitancia() != null){
            capacitorExistente.setCapacitancia(dto.getCapacitancia());
        }
        if(dto.getTensaoMaxima() != null){
            capacitorExistente.setTensaoMaxima(dto.getTensaoMaxima());
        }
        if(dto.getTolerancia() != null){
            capacitorExistente.setTolerancia(dto.getTolerancia());
        }
        if(dto.getUnidadeDeMedida() != null){
            capacitorExistente.setUnidadeDeMedida(dto.getUnidadeDeMedida());
        }
        return repository.save(capacitorExistente);
    }
}
