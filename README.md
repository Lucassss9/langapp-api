# LangApp API

Backend do **LangApp**, uma plataforma de prática de idiomas em que o aluno conversa com NPCs controlados por IA dentro do Minecraft.

> **Status:** em desenvolvimento...

## Sobre o projeto

A ideia é que o aluno pratique conversando com NPCs dentro do Minecraft, em vez de apenas fazer exercícios.

A primeira versão cobre apenas **idiomas** e **interação por texto**. Voz fica para uma etapa futura.

## Status

**API de domínio (Java/Spring Boot)**

Implementado:
- Entidades: Student, Skill, Activity, Attempt, Mistake, Npc, Session e Mastery, com enums de domínio (Verdict, ErrorType)
- Repositórios JPA, incluindo consultas de domínio (Mastery)
- CRUD de alunos, com listagem, cadastro e consulta
- Tratamento global de exceções com Problem Details
- Documentação da API com Swagger UI
- Testes do repositório de Mastery

Planejado:
- Regras de negócio
- Migração para PostgreSQL
- Docker
- Autenticação
- Deploy

**Serviço de IA e mod:** em desenvolvimento por Bryan. A memória de conversa funciona sem banco de dados por enquanto.

## Tecnologias

**API:** Java, Spring Boot, Spring Data JPA, Swagger UI

**Serviço de IA:** Python, FastAPI, Ollama

**Mod:** Fabric (Minecraft 1.20.1)

## Autores

- **Lucas Carvalho** — [GitHub](https://github.com/Lucassss9) · [LinkedIn](https://www.linkedin.com/in/devlucasg)
- **Bryan** — [GitHub](https://github.com/imbryanlima)
