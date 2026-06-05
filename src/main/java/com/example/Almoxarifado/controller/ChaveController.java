package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.AtualizarChaveDTO;
import com.example.Almoxarifado.model.Chave;
import com.example.Almoxarifado.repository.ChaveRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/chaves")
public class ChaveController {
    
    @Autowired
    private ChaveRepository repository;

    @GetMapping
    public List<Chave> consultarTodasChaves() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Chave consultarChave(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Chave cadastrarChave(@Valid @RequestBody Chave novaChave){
        novaChave.setSala(novaChave.getSala().trim().toUpperCase());
        return repository.save(novaChave);
    }

    @PatchMapping("/{id}")
    public Chave atualizarChave(@Valid @RequestBody AtualizarChaveDTO dto, @PathVariable Long id) throws NotFoundException{
        Chave chaveExistente = this.consultarChave(id);

        if(dto.getSala() != null && !dto.getSala().trim().isEmpty()){
            chaveExistente.setSala(dto.getSala().trim().toUpperCase());
        }
        if(dto.getStatus() != null){
            chaveExistente.setStatus(dto.getStatus());
        }
        return repository.save(chaveExistente);
    }

    @DeleteMapping("/{id}")
    public String deletarChave(@PathVariable Long id) throws NotFoundException{
        Chave chaveExistente = this.consultarChave(id);
        repository.delete(chaveExistente);
        return "Chave deletada com sucesso";
    }
}
