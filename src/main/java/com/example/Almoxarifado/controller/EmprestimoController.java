package com.example.Almoxarifado.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Almoxarifado.common.enums.StatusEmprestimoEnum;
import com.example.Almoxarifado.dto.AtualizarEmprestimoDTO;
import com.example.Almoxarifado.dto.CadastrarEmprestimoDTO;
import com.example.Almoxarifado.model.Componente;
import com.example.Almoxarifado.model.Emprestimo;
import com.example.Almoxarifado.model.ItemEmprestavel;
import com.example.Almoxarifado.model.Pessoa;
import com.example.Almoxarifado.model.Usuario;
import com.example.Almoxarifado.repository.EmprestimoRepository;
import com.example.Almoxarifado.repository.ItemEmprestavelRepository;
import com.example.Almoxarifado.repository.PessoaRepository;
import com.example.Almoxarifado.repository.UsuarioRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController{
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ItemEmprestavelRepository itemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Emprestimo> consultarTodosEmprestimos(
        @RequestParam(required = false) String status
    ){
        if(status != null){
            return emprestimoRepository.findByStatus(status);
        }
        return emprestimoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprestimo consultarEmprestimoId(@PathVariable Long id) throws NotFoundException{
        return emprestimoRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @PostMapping
    public Emprestimo cadastrarEmprestimo(@Valid @RequestBody CadastrarEmprestimoDTO dto) throws NotFoundException, BadRequestException{
        Pessoa solicitante = pessoaRepository.findByCpf(dto.getCpfSolicitante())
            .orElseThrow(() -> new NotFoundException());

        Usuario funcionario = usuarioRepository.findById(dto.getFuncionario().getId())
            .orElseThrow(() -> new NotFoundException());

        ItemEmprestavel item = itemRepository.findById(dto.getItemEmprestado().getId())
            .orElseThrow(() -> new NotFoundException());

        if(item instanceof Componente componente){
            if(componente.getQntDisponivel() < dto.getQuantidade()){
                throw new BadRequestException("Estoque insuficiente.");
            }
            componente.setQntDisponivel(componente.getQntDisponivel() - dto.getQuantidade());
        }
        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setDataLimite(dto.getDataLimite());
        novoEmprestimo.setDataRetirada(dto.getDataRetirada());
        novoEmprestimo.setSolicitante(solicitante);
        novoEmprestimo.setFuncionario(funcionario);
        novoEmprestimo.setItemEmprestado(item);
        novoEmprestimo.setQuantidade(dto.getQuantidade());
        novoEmprestimo.setStatus(StatusEmprestimoEnum.ATIVO.name());
        
        return emprestimoRepository.save(novoEmprestimo);
    }

    @PatchMapping("/{id}")
    public Emprestimo atualizarEmprestimo(@Valid @RequestBody AtualizarEmprestimoDTO dto, @PathVariable Long id) throws NotFoundException{
        Emprestimo emprestimo = this.consultarEmprestimoId(id);

        if(dto.getDataDevolucao() != null){
            emprestimo.setDataDevolucao(dto.getDataDevolucao());
        }
        if(dto.getStatus() != null){
            emprestimo.setStatus(dto.getStatus());
        }
        return emprestimoRepository.save(emprestimo);
    }

    @DeleteMapping("/{id}") 
    public String deletarEmprestimo(@PathVariable Long id) throws NotFoundException {
        Emprestimo EmprestimoExistente = this.consultarEmprestimoId(id);
        emprestimoRepository.delete(EmprestimoExistente);
        return "Emprestimo deletado com sucesso.";
    }
}