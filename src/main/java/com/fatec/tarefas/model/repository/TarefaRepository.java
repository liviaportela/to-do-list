package com.fatec.tarefas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.tarefas.model.entity.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
