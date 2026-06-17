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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.dto.AtualizarDocenteDTO;
import com.example.Almoxarifado.model.Docente;
import com.example.Almoxarifado.model.Pessoa;
import com.example.Almoxarifado.repository.DocenteRepository;
import com.example.Almoxarifado.repository.PessoaRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController
@RequestMapping("/docente")
public class DocenteController {
    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Docente> consultarTodosDocentes(){
        return docenteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Docente consultarDocenteId(@PathVariable Long id) throws NotFoundException{
        return docenteRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }
    
    @GetMapping("/filtro/{siape}") // aqui vai encontrar o docente usando o siape, espero que funcione xd
    public Docente consultarDocenteSiape(@PathVariable String siape) throws NotFoundException {
        return docenteRepository.findBySiape(siape).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Docente cadastrarDocente(@Valid @RequestBody Docente novoDocente) throws BadRequestException{
        Optional<Pessoa> cpfExistente = pessoaRepository.findByCpf(novoDocente.getCpf());
        if(cpfExistente.isPresent()){
            throw new BadRequestException("CPF já cadastrado no sistema.");
        }
        Optional<Pessoa> emailExistente = pessoaRepository.findByEmail(novoDocente.getEmail());
        if(emailExistente.isPresent()){
            throw new BadRequestException("Email já cadastrado no sistema.");
        }
        Optional<Docente> siapeExistente = docenteRepository.findBySiape(novoDocente.getSiape());
        if(siapeExistente.isPresent()){
            throw new BadRequestException("SIAPE já cadastrado no sistema.");
        }
        return docenteRepository.save(novoDocente);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Docente atualizarDocente(@Valid @RequestBody AtualizarDocenteDTO dto, @PathVariable Long id) throws NotFoundException, BadRequestException{
        Docente docenteExistente = docenteRepository.findById(id).orElseThrow(() -> new NotFoundException());

        if(dto.getCpf() != null && !dto.getCpf().equals(docenteExistente.getCpf())){
            Optional<Pessoa> cpfExistente = pessoaRepository.findByCpf(dto.getCpf());
            if(cpfExistente.isPresent()){
                throw new BadRequestException("CPF já cadastrado no sistema.");
            }
            docenteExistente.setCpf(dto.getCpf());
        }
        if(dto.getEmail() != null && !dto.getEmail().equals(docenteExistente.getEmail())){
            Optional<Pessoa> emailExistente = pessoaRepository.findByEmail(dto.getEmail());
            if(emailExistente.isPresent()){
                throw new BadRequestException("Email já cadastrado no sistema.");
            }
            docenteExistente.setEmail(dto.getEmail());
        }
        if(dto.getSiape() != null && !dto.getSiape().equals(docenteExistente.getSiape())){
            Optional<Docente> siapeExistente = docenteRepository.findBySiape(dto.getSiape());
            if(siapeExistente.isPresent()){
                throw new BadRequestException("SIAPE já cadastrado no sistema.");
            }
            docenteExistente.setSiape(dto.getSiape());
        }
        if(dto.getNome() != null){
            docenteExistente.setNome(dto.getNome());
        }

        return docenteRepository.save(docenteExistente);
    }
}
