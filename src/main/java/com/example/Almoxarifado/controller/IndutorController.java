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

import com.example.Almoxarifado.common.enums.UnidadeDeMedidaEnum;
import com.example.Almoxarifado.dto.AtualizarIndutorDTO;
import com.example.Almoxarifado.model.Indutor;
import com.example.Almoxarifado.repository.IndutorRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/indutor")
@CrossOrigin(origins = "*")
public class IndutorController {
    @Autowired
    private IndutorRepository repository;

    @GetMapping
    public List<Indutor> consultarTodosIndutores(
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
                    return repository.findByIndutanciaGreaterThanEqualAndUnidadeDeMedida(min, unidade);
                case 6:
                    return repository.findByIndutanciaLessThanEqualAndUnidadeDeMedida(max, unidade);
                case 7:
                    return repository.findByIndutanciaBetweenAndUnidadeDeMedida(min, max, unidade);
                default:
                    return List.of();
            }
        }
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

        if(dto.getModelo() != null){
            indutorExistente.setModelo(dto.getModelo());
        }
        if(dto.getDescricao() != null){
            indutorExistente.setDescricao(dto.getDescricao());
        }
        if(dto.getQntDisponivel() != null){
            indutorExistente.setQntDisponivel(dto.getQntDisponivel());
        }
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
