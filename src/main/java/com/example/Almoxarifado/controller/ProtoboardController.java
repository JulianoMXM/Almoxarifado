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

import com.example.Almoxarifado.dto.AtualizarProtoboardDTO;
import com.example.Almoxarifado.model.Protoboard;
import com.example.Almoxarifado.repository.ProtoboardRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/protoboard")
public class ProtoboardController {
    @Autowired
    private ProtoboardRepository repository;

    @GetMapping
    public List<Protoboard> consultarTodasProtoboards(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Protoboard consultarProtoboard(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Protoboard cadastrarProtoboard(@Valid @RequestBody Protoboard novoProtoboard){
        return repository.save(novoProtoboard);
    }

    @PatchMapping("/{id}")
    public Protoboard atualizarProtoboard(@Valid @RequestBody AtualizarProtoboardDTO dto, @PathVariable Long id) throws NotFoundException{
        Protoboard protoboardExistente = repository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getModelo() != null){
            protoboardExistente.setModelo(dto.getModelo());
        }
        if(dto.getDescricao() != null){
            protoboardExistente.setDescricao(dto.getDescricao());
        }
        if(dto.getQntDisponivel() != null){
            protoboardExistente.setQntDisponivel(dto.getQntDisponivel());
        }
        if(dto.getTensaoMaxima() != null){
            protoboardExistente.setTensaoMaxima(dto.getTensaoMaxima());
        }
        if(dto.getCorrenteMaxima() != null){
            protoboardExistente.setCorrenteMaxima(dto.getCorrenteMaxima());
        }
        if(dto.getPotenciaMaxima() != null){
            protoboardExistente.setPotenciaMaxima(dto.getPotenciaMaxima());
        }
        return repository.save(protoboardExistente);
    }
}
