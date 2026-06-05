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

import com.example.Almoxarifado.dto.AtualizarCapacitorDTO;
import com.example.Almoxarifado.model.Capacitor;
import com.example.Almoxarifado.repository.CapacitorRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/capacitor")
public class CapacitorController {
    @Autowired
    private CapacitorRepository repository;

    @GetMapping
    public List<Capacitor> consultarTodosCapacitores(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Capacitor consultarCapacitor(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Capacitor cadastrarCapacitor(@Valid @RequestBody Capacitor novoCapacitor){
        return repository.save(novoCapacitor);
    }

    @PatchMapping("/{id}")
    public Capacitor atualizarCapacitor(@Valid @RequestBody AtualizarCapacitorDTO dto, @PathVariable Long id) throws NotFoundException{
        Capacitor capacitorExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getModelo() != null){
            capacitorExistente.setModelo(dto.getModelo());
        }
        if(dto.getDescricao() != null){
            capacitorExistente.setDescricao(dto.getDescricao());
        }
        if(dto.getQntDisponivel() != null){
            capacitorExistente.setQntDisponivel(dto.getQntDisponivel());
        }
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
