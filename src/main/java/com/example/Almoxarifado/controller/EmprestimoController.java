package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.Emprestimo;
import com.example.Almoxarifado.repository.EmprestimoRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/emprestimo")

public class EmprestimoController{
    @Autowired
    private EmprestimoRepository repository;

    @GetMapping
    public List<Emprestimo> consultarTodosEmprestimos(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Emprestimo consultarEmprestimoId(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @GetMapping("/Emprestimo/{status}")
    public List<Emprestimo> consultarEmprestimoStatus(@PathVariable String status){
        return repository.findByStatus(status);
    }

    @PostMapping
    public Emprestimo cadastrarEmprestimo(@Valid @RequestBody Emprestimo novoEmprestimo){
        return repository.save(novoEmprestimo);
    }

    @DeleteMapping("/{id}") 
    public String deletarEmprestimo(@PathVariable Long id) throws NotFoundException {
        Emprestimo EmprestimoExistente = this.consultarEmprestimoId(id);
        repository.delete(EmprestimoExistente);
        return "Emprestimo deletado com sucesso.";
    }
}