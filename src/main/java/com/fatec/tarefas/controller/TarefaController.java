package com.fatec.tarefas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.tarefas.dto.TarefaDTO;
import com.fatec.tarefas.model.entity.Tarefa;
import com.fatec.tarefas.service.TarefaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criar(@Valid @RequestBody TarefaDTO dto) {
        return service.criar(converter(dto));
    }

    @GetMapping
    public List<Tarefa> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody TarefaDTO dto) {
        return service.atualizar(id, converter(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    private Tarefa converter(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(dto.nome());
        tarefa.setDescricao(dto.descricao());
        tarefa.setStatus(dto.status());
        tarefa.setObservacoes(dto.observacoes());
        return tarefa;
    }
}
