package com.example.Almoxarifado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.model.ItemEmprestavel;
import com.example.Almoxarifado.repository.ItemEmprestavelRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/itemEmprestavel")
@CrossOrigin(origins = "*")
public class ItemEmprestavelController {
    @Autowired
    private ItemEmprestavelRepository repository;

    @GetMapping("/{id}")
    public ItemEmprestavel consultarItemEmprestavel(@PathVariable Long id) throws NotFoundException{
        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }
}
