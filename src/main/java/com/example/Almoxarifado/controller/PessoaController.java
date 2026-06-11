package com.example.Almoxarifado.controller;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.Pessoa;
import com.example.Almoxarifado.repository.PessoaRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Slf4j
@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaRepository repository;

    @GetMapping
    public List<Pessoa> consultarTodasPessoas(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pessoa consultarPessoaId(@PathVariable Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }
    
    @GetMapping("/filtro/{cpf}") // espero que essa minha lógica funcione, pq se não vou ficar lelé
    public Pessoa consultarPessoaCpf(@PathVariable String cpf) throws NotFoundException {
        return repository.findByCpf(cpf).orElseThrow(() -> new NotFoundException());
    }

    @DeleteMapping("/{id}") 
    public String deletarPessoa(@PathVariable Long id) throws NotFoundException {
        Pessoa pessoaExistente = this.consultarPessoaId(id);
        repository.delete(pessoaExistente);
        return "Pessoa deletada com sucesso.";
    }
}
