package com.fatec.tarefas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fatec.tarefas.exception.RegraNegocioException;
import com.fatec.tarefas.exception.TarefaNaoEncontradaException;
import com.fatec.tarefas.model.entity.Tarefa;
import com.fatec.tarefas.model.enums.StatusTarefa;
import com.fatec.tarefas.model.repository.TarefaRepository;
import com.fatec.tarefas.service.impl.TarefaServiceImpl;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaServiceImpl service;

    private Tarefa tarefa(String nome) {
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(nome);
        return tarefa;
    }

    @Test
    void criarDefineStatusPendenteQuandoNaoInformado() {
        when(repository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarefa salva = service.criar(tarefa("Estudar"));

        assertEquals(StatusTarefa.PENDENTE, salva.getStatus());
    }

    @Test
    void criarSemNomeLancaExcecaoSemSalvar() {
        assertThrows(RegraNegocioException.class, () -> service.criar(tarefa(" ")));

        verify(repository, never()).save(any(Tarefa.class));
    }

    @Test
    void atualizarAlteraCamposInformados() {
        Tarefa existente = tarefa("Antigo");
        existente.setStatus(StatusTarefa.PENDENTE);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarefa dados = tarefa("Novo");
        dados.setStatus(StatusTarefa.CONCLUIDA);
        dados.setDescricao("Descrição atualizada");
        dados.setObservacoes("Feito");

        Tarefa atualizada = service.atualizar(1L, dados);

        assertEquals("Novo", atualizada.getNome());
        assertEquals(StatusTarefa.CONCLUIDA, atualizada.getStatus());
        assertEquals("Descrição atualizada", atualizada.getDescricao());
        assertEquals("Feito", atualizada.getObservacoes());
    }

    @Test
    void atualizarPreservaStatusQuandoNaoInformado() {
        Tarefa existente = tarefa("Antigo");
        existente.setStatus(StatusTarefa.EM_ANDAMENTO);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarefa atualizada = service.atualizar(1L, tarefa("Novo"));

        assertEquals(StatusTarefa.EM_ANDAMENTO, atualizada.getStatus());
    }

    @Test
    void atualizarTarefaInexistenteLancaExcecao() {
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> service.atualizar(9L, tarefa("Nova")));
    }

    @Test
    void deletarRemoveTarefaExistente() {
        Tarefa existente = tarefa("Excluir");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        service.deletar(1L);

        verify(repository).delete(existente);
    }

    @Test
    void deletarTarefaInexistenteLancaExcecao() {
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> service.deletar(9L));
    }
}
