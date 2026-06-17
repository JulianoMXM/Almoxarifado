package com.example.Almoxarifado.controller;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.AtualizarDiscenteDTO;
import com.example.Almoxarifado.model.Discente;
import com.example.Almoxarifado.model.Pessoa;
import com.example.Almoxarifado.repository.DiscenteRepository;
import com.example.Almoxarifado.repository.PessoaRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/discente")
public class DiscenteController {
    @Autowired
    private DiscenteRepository discenteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Discente> consultarTodosDiscentes() {
        return discenteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Discente consultarDiscenteId(@PathVariable Long id) throws NotFoundException {
        return discenteRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @GetMapping("/filtro/{ra}")
    public Discente consultarDiscenteRa(@PathVariable String ra) throws NotFoundException {
        return discenteRepository.findByRa(ra).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Discente cadastrarDiscente(@Valid @RequestBody Discente novoDiscente) throws BadRequestException{
        Optional<Pessoa> cpfExistente = pessoaRepository.findByCpf(novoDiscente.getCpf());
        if(cpfExistente.isPresent()){
            throw new BadRequestException("CPF já cadastrado no sistema.");
        }
        Optional<Pessoa> emailExistente = pessoaRepository.findByEmail(novoDiscente.getEmail());
        if(emailExistente.isPresent()){
            throw new BadRequestException("Email já cadastrado no sistema.");
        }
        return discenteRepository.save(novoDiscente);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Discente atualizarDiscente(@Valid @RequestBody AtualizarDiscenteDTO dto, @PathVariable Long id) throws NotFoundException, BadRequestException {
        Discente discenteExistente = discenteRepository.findById(id).orElseThrow(() -> new NotFoundException());
        
        if(dto.getCpf() != null && !dto.getCpf().equals(discenteExistente.getCpf())){
            Optional<Pessoa> cpfExistente = pessoaRepository.findByCpf(dto.getCpf());
            if(cpfExistente.isPresent()){
                throw new BadRequestException("CPF já cadastrado no sistema.");
            }
            discenteExistente.setCpf(dto.getCpf());
        }
        if(dto.getEmail() != null && !dto.getEmail().equals(discenteExistente.getEmail())){
            Optional<Pessoa> emailExistente = pessoaRepository.findByEmail(dto.getEmail());
            if(emailExistente.isPresent()){
                throw new BadRequestException("Email já cadastrado no sistema.");
            }
            discenteExistente.setEmail(dto.getEmail());
        }
        if(dto.getRa() != null && !dto.getRa().equals(discenteExistente.getRa())){
            Optional<Discente> raExistente = discenteRepository.findByRa(dto.getRa());
            if(raExistente.isPresent()){
                throw new BadRequestException("RA já cadastrado no sistema.");
            }
            discenteExistente.setRa(dto.getRa());
        }
        if(dto.getNome() != null){
            discenteExistente.setNome(dto.getNome());
        }

        return discenteRepository.save(discenteExistente);
    }
}
