package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.AtualizarResistorDTO;
import com.example.Almoxarifado.model.Resistor;
import com.example.Almoxarifado.repository.ResistorRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/resistor")
@CrossOrigin(origins = "*")
public class ResistorController {
    @Autowired
    private ResistorRepository repository;

    @GetMapping
    public List<Resistor> consultarTodasResistores(
        @RequestParam(required = false) Double min,
        @RequestParam(required = false) Double max,
        @RequestParam(required = false) String unidade
    ){
        if(min == null && max == null && unidade == null){
             return repository.findAll();
        } else {
            int aux = 0;
            if(min != null) aux += 1;
            if(max != null) aux += 2;
            if(unidade != null) aux += 4;

            switch(aux){
                case 4:
                    return repository.findByUnidadeDeMedida(unidade);
                case 5:
                    return repository.findByResistenciaGreaterThanEqualAndUnidadeDeMedida(min, unidade);
                case 6:
                    return repository.findByResistenciaLessThanEqualAndUnidadeDeMedida(max, unidade);
                case 7:
                    return repository.findByResistenciaBetweenAndUnidadeDeMedida(min, max, unidade);
                default:
                    return List.of();
            }
        }
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

        if(dto.getModelo() != null){
            resistorExistente.setModelo(dto.getModelo());
        }
        if(dto.getDescricao() != null){
            resistorExistente.setDescricao(dto.getDescricao());
        }
        if(dto.getQntDisponivel() != null){
            resistorExistente.setQntDisponivel(dto.getQntDisponivel());
        }
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
