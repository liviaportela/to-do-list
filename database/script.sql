-- Etapa 1: criação do banco de dados da Lista de Tarefas
-- Execute conectado ao PostgreSQL como um usuário com permissão para criar bancos:
-- psql -U postgres -f database/script.sql

CREATE DATABASE tarefas_db;
\connect tarefas_db

CREATE TABLE IF NOT EXISTS tarefa (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(150) NOT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE'
                      CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA')),
    descricao         TEXT,
    observacoes       TEXT,
    data_criacao      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
