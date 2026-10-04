package com.fatec.tarefas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TarefaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private long criar(String json) throws Exception {
        MvcResult resultado = mvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        return mapper.readTree(resultado.getResponse().getContentAsString())
                .get("id")
                .asLong();
    }

    @Test
    void criaTarefaComStatusPadraoEDatas() throws Exception {
        mvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Estudar\",\"descricao\":\"Spring\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Estudar"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.dataCriacao").exists())
                .andExpect(jsonPath("$.dataAtualizacao").exists());
    }

    @Test
    void rejeitaTarefaSemNome() throws Exception {
        mvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void rejeitaNomeMaiorQueLimite() throws Exception {
        String nome = "a".repeat(151);

        mvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(java.util.Map.of("nome", nome))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscaTarefaPorId() throws Exception {
        long id = criar("{\"nome\":\"Buscar\"}");

        mvc.perform(get("/api/tarefas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Buscar"));
    }

    @Test
    void buscaTarefaInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/tarefas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void atualizaTarefa() throws Exception {
        long id = criar("{\"nome\":\"Antiga\"}");

        mvc.perform(put("/api/tarefas/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nova\",\"status\":\"CONCLUIDA\",\"observacoes\":\"Feita\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nova"))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"))
                .andExpect(jsonPath("$.observacoes").value("Feita"));
    }

    @Test
    void atualizaTarefaInexistenteRetorna404() throws Exception {
        mvc.perform(put("/api/tarefas/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nova\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaTarefas() throws Exception {
        criar("{\"nome\":\"Primeira\"}");

        mvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Primeira"));
    }

    @Test
    void deletaTarefa() throws Exception {
        long id = criar("{\"nome\":\"Excluir\"}");

        mvc.perform(delete("/api/tarefas/" + id))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/tarefas/" + id))
                .andExpect(status().isNotFound());
    }
}
