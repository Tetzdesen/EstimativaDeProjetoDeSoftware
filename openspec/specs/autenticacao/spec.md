# autenticacao Specification

## Purpose
Identificar o desenvolvedor que usa o sistema (casos de uso Fazer Login e Criar Novo Usuário; RNF09).

## Requirements

### Requirement: Criar novo usuário
O sistema SHALL permitir o cadastro de um usuário com nome, e-mail e senha, validando a senha com o validador de senhas incluído via JitPack e envolvido por um Adapter.

#### Scenario: Cadastro válido
- **WHEN** o usuário informa nome, e-mail ainda não cadastrado e senha aceita pelo validador
- **THEN** o sistema cria a conta e permite o login

#### Scenario: Senha rejeitada
- **WHEN** a senha não atende às regras do validador
- **THEN** o sistema não cria a conta e exibe as mensagens retornadas pelo validador

#### Scenario: E-mail duplicado
- **WHEN** o e-mail já pertence a outro usuário
- **THEN** o sistema recusa o cadastro e informa o motivo

### Requirement: Fazer login
O sistema SHALL autenticar o usuário por e-mail e senha e liberar as funcionalidades apenas após o login.

#### Scenario: Credenciais válidas
- **WHEN** o usuário informa e-mail e senha corretos
- **THEN** a janela principal MDI é aberta
- **AND** o rodapé exibe o nome do usuário logado

#### Scenario: Credenciais inválidas
- **WHEN** o e-mail não existe ou a senha está errada
- **THEN** o sistema exibe uma mensagem genérica de falha sem indicar qual campo está errado
