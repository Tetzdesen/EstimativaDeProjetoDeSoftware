# Estimativa De Projeto De Software

O trabalho final tem como objetivo o desenvolvimento de um software para auxiliar desenvolvedores na estimativa de custos e tempo para criação de aplicativos.

## Como executar

1. Copie o arquivo de exemplo de variáveis de ambiente:

   ```bash
   cp .env.example .env
   ```

   O `.env` não é versionado. O `.env.example` traz a configuração padrão de
   SQLite em arquivo, e a chave com o nome do banco (`SQLite`) precisa apontar
   para a fábrica de repositórios correspondente.

2. Compile e rode os testes:

   ```bash
   mvn test
   ```

3. Execute a aplicação:

   ```bash
   mvn exec:java
   ```

Na primeira execução o banco é criado em `db/database.db`, com as tabelas e os
dados iniciais. As senhas são gravadas como hash PBKDF2, então elas nunca ficam
em texto puro no banco.
