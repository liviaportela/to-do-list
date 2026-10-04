package com.fatec.tarefas.service;

import java.util.List;

import com.fatec.tarefas.model.entity.Tarefa;

public interface TarefaService {

    Tarefa criar(Tarefa tarefa);

    Tarefa atualizar(Long id, Tarefa dados);

    void deletar(Long id);

    Tarefa buscarPorId(Long id);

    List<Tarefa> listar();
}
