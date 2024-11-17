# Projeto Contas a pagar 

Este projeto é um serviço de gerenciamento de contas a pagar, que permite cadastrar, atualizar, consultar e importar contas via arquivo CSV. A aplicação utiliza **Java 17+, Spring Boot, PostgreSQL**, e é orquestrada com **Docker e Docker Compose**. Além disso, a documentação da API é gerada automaticamente utilizando **Swagger**.

## Tecnologias Utilizadas

- **Java 17+**: A versão 17 ou superior do Java é utilizada para o desenvolvimento da aplicação.
- **Spring Boot**: Framework utilizado para criar a aplicação de forma rápida e eficiente.
- **PostgreSQL**: Banco de dados relacional utilizado para armazenar as contas a pagar.
- **Docker**: Para criar contêineres que executam a aplicação, banco de dados e outros serviços necessários.
- **Docker Compose**: Para orquestrar os contêineres, facilitando o processo de execução de múltiplos serviços (aplicação, banco de dados, etc.).
- **Flyway**: Ferramenta utilizada para gerenciar migrações de banco de dados.
- **JPA (Java Persistence API)**: Para realizar o mapeamento objeto-relacional e persistir os dados no banco de dados.
- **Spring Security**: Para implementar o mecanismo de autenticação da aplicação.
- **Swagger**: Para gerar e documentar automaticamente a API REST da aplicação.
- **JUnit 5** e **Mockito**: Frameworks utilizados para escrever testes unitários e simular comportamentos durante os testes.

## Requisitos Gerais

1. **Java 17+**: A aplicação foi desenvolvida com Java versão 17 ou superior.
2. **Spring Boot**: Utilizado para estruturar a aplicação e facilitar o desenvolvimento rápido.
3. **PostgreSQL**: Banco de dados relacional utilizado para armazenar as contas.
4. **Docker**: A aplicação é executada dentro de um contêiner Docker.
5. **Docker Compose**: Utilizado para orquestrar a aplicação e o banco de dados, além de outros serviços necessários.
6. **GitHub/GitLab**: O código do projeto está hospedado em um repositório GitHub ou GitLab.
7. **Autenticação**: O serviço utiliza um mecanismo de autenticação para proteger as APIs.
8. **Domain Driven Design (DDD)**: O projeto foi estruturado com base nos princípios de DDD para organizar o código.
9. **Flyway**: Usado para gerenciar e aplicar as migrações do banco de dados.
10. **JPA**: Utilizado para mapear e persistir as entidades no banco de dados.
11. **Paginação nas APIs de consulta**: Todas as APIs de consulta são paginadas.

## Requisitos Específicos

1. **Tabela de Contas a Pagar**:
   - A tabela no banco de dados para armazenar as contas a pagar inclui os seguintes campos:
     - `id` (Identificador único da conta)
     - `data_vencimento` (Data de vencimento da conta)
     - `data_pagamento` (Data de pagamento da conta)
     - `valor` (Valor da conta)
     - `descricao` (Descrição da conta)
     - `situacao` (Situação da conta: "Pendente", "Paga", etc.)

2. **Entidade Conta**:
   - A entidade `Conta` foi implementada de acordo com a tabela de banco de dados e mapeada utilizando JPA.

3. **APIs Implementadas**:

   - **Cadastrar Conta**: API para cadastrar uma nova conta a pagar.
   - **Atualizar Conta**: API para atualizar os dados de uma conta existente.
   - **Alterar Situação da Conta**: API para alterar a situação de uma conta (ex: de "Pendente" para "Paga").
   - **Obter Lista de Contas**: API para obter uma lista paginada de contas a pagar, com filtros para data de vencimento e descrição.
   - **Obter Conta por ID**: API para obter uma conta específica pelo seu `id`.
   - **Obter Valor Total Pago por Período**: API para calcular o valor total das contas pagas em um determinado período.

4. **Importação de Contas via CSV**:
   - A aplicação permite importar contas a pagar a partir de um arquivo CSV. O arquivo CSV é consumido via API, onde as contas são processadas e salvas no banco de dados.

5. **Testes Unitários**:
   - O projeto inclui testes unitários utilizando JUnit 5 e Mockito para garantir a correta funcionalidade das APIs e da importação de CSV.

## Estrutura do Projeto

A estrutura do projeto segue o padrão **Domain Driven Design (DDD)**, com as seguintes principais pastas e arquivos:
src/
 └── main/
      ├── java/
      │    └── com/
      │         └── seuusuario/
      │              └── contaservice/
      │                   ├── Conta.java            # Entidade de Conta
      │                   ├── ContaService.java      # Lógica de importação e manipulação de contas
      │                   ├── ContaRepository.java   # Repositório de contas (interação com o banco)
      │                   ├── ContaController.java   # Controlador de API (para expor endpoints)
      │                   ├── ContaMapper.java       # Mapeamento entre CSV e entidade Conta (se necessário)
      │                   └── SecurityConfig.java    # Configuração de autenticação
      └── resources/
           ├── application.properties  # Configurações da aplicação
           ├── db/migration/           # Scripts do Flyway para migrações do banco
           └── docker-compose.yml      # Arquivo de orquestração Docker Compose

## Como Rodar o Projeto

### Pré-requisitos

- **Docker**: Para rodar a aplicação e o banco de dados no Docker.
- **Docker Compose**: Para orquestrar os contêineres necessários (aplicação e banco de dados).

### Passos para Rodar

1. Clone o repositório:

    ```bash
    git clone https://github.com/seu-usuario/projeto-conta-service.git
    cd projeto-conta-service
    ```

2. Construa os contêineres Docker e inicie a aplicação:

    ```bash
    docker-compose up --build
    ```

3. A aplicação será executada no `http://localhost:8080`, e o banco de dados PostgreSQL estará disponível para uso.

### Rodando Localmente (sem Docker)

Se preferir rodar a aplicação localmente (sem Docker), basta executar os seguintes comandos:

1. Compile o projeto:

    ```bash
    mvn clean install
    ```

2. Execute a aplicação:

    ```bash
    mvn spring-boot:run
    ```

3. A aplicação estará acessível em `http://localhost:8080`.

## Testes

Para rodar os testes unitários da aplicação, utilize o comando Maven:

```bash
mvn test

##### Todos direitos reservados Samael Simões
