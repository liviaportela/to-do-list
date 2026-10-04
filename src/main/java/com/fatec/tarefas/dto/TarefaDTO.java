package com.fatec.tarefas.dto;

import com.fatec.tarefas.model.enums.StatusTarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaDTO(
        @NotBlank @Size(max = 150) String nome,
        String descricao,
        StatusTarefa status,
        String observacoes) {
}
