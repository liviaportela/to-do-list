# Gerenciador de Tarefas

API REST para criar e gerenciar tarefas do dia a dia. Não possui login nem usuários.

## Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- H2 para testes

## Banco de dados

O script [`database/script.sql`](database/script.sql) cria o banco `tarefas_db` e a tabela `tarefa`.

## Executar a aplicação

Na raiz do projeto, defina a senha do PostgreSQL e inicie o Spring Boot.


A API ficará disponível em `http://localhost:8080`. Mantenha o terminal aberto enquanto utiliza a aplicação.

## Testar no Postman

Base URL: `http://localhost:8080/api/tarefas`

| Método | Caminho | Ação |
|---|---|---|
| `POST` | `/api/tarefas` | Criar tarefa |
| `GET` | `/api/tarefas` | Listar tarefas |
| `GET` | `/api/tarefas/{id}` | Consultar uma tarefa |
| `PUT` | `/api/tarefas/{id}` | Atualizar uma tarefa |
| `DELETE` | `/api/tarefas/{id}` | Excluir uma tarefa |

Para `POST` e `PUT`, json de Exemplo:

```json
{
  "nome": "Estudar Spring",
  "descricao": "Revisar a API",
  "status": "PENDENTE",
  "observacoes": "Testar no Postman"
}
```

O nome é obrigatório e pode ter até 150 caracteres. Os status aceitos são `PENDENTE`, `EM_ANDAMENTO` e `CONCLUIDA`. Se não for informado na criação, o status será `PENDENTE`; as datas de criação e atualização são preenchidas automaticamente.

## Testes automatizados

Execute na raiz. Os testes de integração usam H2 em memória e não precisam do PostgreSQL.
