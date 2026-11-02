# persistencia Specification

## Purpose
Armazenar os dados sem framework de persistência, com bancos intercambiáveis (RNF02, RNF03).

## Requirements

### Requirement: Bancos intercambiáveis
O sistema SHALL funcionar com SQLite e com H2, escolhidos por arquivo de configuração, sem instalação adicional e sem Docker.

#### Scenario: Troca de banco
- **WHEN** o arquivo de configuração é alterado de SQLite para H2
- **THEN** o sistema inicia com H2 e executa o seeder

#### Scenario: Seeder idempotente
- **WHEN** o sistema reinicia com o banco já populado
- **THEN** o seeder não duplica o perfil padrão

### Requirement: Repository com JDBC
O acesso a dados SHALL usar o padrão Repository com uma implementação JDBC por banco, sem Hibernate, JPA ou similares.

#### Scenario: Exceções de banco
- **WHEN** ocorre uma falha de banco em um repositório
- **THEN** a exceção é tratada e o usuário recebe uma mensagem clara, sem fechar o sistema
