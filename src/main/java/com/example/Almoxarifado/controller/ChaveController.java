package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.common.util.ErrorHandlerUtil;
import com.example.Almoxarifado.model.Chave;
import com.example.Almoxarifado.repository.ChaveRepository;

@RestController
@RequestMapping("/chaves")
public class ChaveController {
    
    @Autowired
    private ChaveRepository repository;

    @GetMapping
    public List<Chave> consultarTodasChaves() {
        try{
            return repository.findAll();
        } catch(Exception exception){
            throw ErrorHandlerUtil.handleError("ChaveController - consultarTodasChaves", exception, null);
        }
    }

    @GetMapping
    public Chave consultarChave(Long id) {
        try{
            Chave chave = repository.findById(id).orElseThrow(() -> new NotFoundException());
            return chave;
        } catch(Exception exception){
            throw ErrorHandlerUtil.handleError("ChaveController - consultarChave", exception, null);
        }
    }

    @PostMapping
    public Chave cadastrarChave(@RequestBody Chave novaChave){
        if(novaChave.getSala().isEmpty()){
            throw
        }
        Chave chave = repository
    }
}
