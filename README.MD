# Hotmart Challenge

## Versão JDK e banco de dados

- Java: 11
- Postgres: latest

## Como rodar o projeto

1. Faça um clone do projeto em seu ambiente local:
  ```git clone https://github.com/mfderson/hotmartchallenge.git```
2. Instale o postgres em um container docker:
3. doker run --name hotmartchallenge -e POSTGRES_PASSWORD=docker -p 5432:5432 -d postgres