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

import com.example.Almoxarifado.dto.AtualizarResistorDTO;
import com.example.Almoxarifado.model.Protoboard;
import com.example.Almoxarifado.model.Resistor;
import com.example.Almoxarifado.repository.ResistorRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/resistor")
public class ResistorController {
    @Autowired
    private ResistorRepository repository;

    @GetMapping
    public List<Resistor> consultarTodasResistores(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Resistor consultarResistor(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Resistor cadastrarResistor(@Valid @RequestBody Resistor novoResistor){
        return repository.save(novoResistor);
    }

    @PatchMapping("/{id}")
    public Resistor atualizarResistor(@Valid @RequestBody AtualizarResistorDTO dto, @PathVariable Long id) throws NotFoundException{
        Resistor resistorExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getResistencia() != null){
            resistorExistente.setResistencia(dto.getResistencia());
        }
        if(dto.getPotenciaMaxima() != null){
            resistorExistente.setPotenciaMaxima(dto.getPotenciaMaxima());
        }
        if(dto.getTolerancia() != null){
            resistorExistente.setTolerancia(dto.getTolerancia());
        }
        if(dto.getUnidadeDeMedida() != null){
            resistorExistente.setUnidadeDeMedida(dto.getUnidadeDeMedida());
        }
        return repository.save(resistorExistente);
    }
}
