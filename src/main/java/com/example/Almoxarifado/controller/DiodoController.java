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

import com.example.Almoxarifado.common.enums.TipoDiodoEnum;
import com.example.Almoxarifado.dto.AtualizarDiodoDTO;
import com.example.Almoxarifado.model.Diodo;
import com.example.Almoxarifado.repository.DiodoRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/diodo")
@CrossOrigin(origins = "*")
public class DiodoController {
    @Autowired
    private DiodoRepository repository;

    @GetMapping
    public List<Diodo> consultarTodosDiodos(
        @RequestParam(required = false) String tipo
    ){
        if(tipo != null){
            return repository.findByTipoDiodo(tipo);
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Diodo consultarDiodo(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Diodo cadastrarDiodo(@Valid @RequestBody Diodo novoDiodo){
        return repository.save(novoDiodo);
    }

    @PatchMapping("/{id}")
    public Diodo atualziarDiodo(@Valid @RequestBody AtualizarDiodoDTO dto, @PathVariable Long id) throws NotFoundException{
        Diodo diodoExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());
    
        if(dto.getModelo() != null){
            diodoExistente.setModelo(dto.getModelo());
        }
        if(dto.getDescricao() != null){
            diodoExistente.setDescricao(dto.getDescricao());
        }
        if(dto.getQntDisponivel() != null){
            diodoExistente.setQntDisponivel(dto.getQntDisponivel());
        }
        if(dto.getTensaoReversaMaxima() != null){
            diodoExistente.setTensaoReversaMaxima(dto.getTensaoReversaMaxima());
        }
        if(dto.getCorrenteDiretaMaxima() != null){
            diodoExistente.setCorrenteDiretaMaxima(dto.getCorrenteDiretaMaxima());
        }
        if(dto.getQuedaDeTensao() != null){
            diodoExistente.setQuedaDeTensao(dto.getQuedaDeTensao());
        }
        if(dto.getTipoDiodo() != null){
            diodoExistente.setTipoDiodo(dto.getTipoDiodo());
        }
        return repository.save(diodoExistente);
    }
}
