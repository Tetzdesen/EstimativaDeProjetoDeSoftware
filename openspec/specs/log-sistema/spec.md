# log-sistema Specification

## Purpose
Registrar em arquivo as operações relevantes do sistema (requisito funcional de log, RNF07, RNF08).

## Requirements

### Requirement: Operações registradas
O sistema SHALL registrar em log criação, alteração e exclusão de projeto, compartilhamento de projeto e exportação de projeto.

#### Scenario: Sucesso
- **WHEN** uma dessas operações conclui
- **THEN** o log recebe "<OPERACAO>: <NOME_PROJETO>, (<DATA>, <HORA>, e <USUARIO>)"

#### Scenario: Falha
- **WHEN** uma dessas operações falha
- **THEN** o log recebe "Ocorreu a falha <MENSAGEM> ao realizar a <OPERACAO> do projeto <NOME_PROJETO>, (<DATA>, <HORA>, e <USUARIO>)."
- **AND** DATA usa DD/MM/AAAA e HORA usa HH:MM:SS do sistema operacional
- **AND** USUARIO é o usuário autenticado

### Requirement: Formatos e módulo independente
O log SHALL suportar CSV separado por ponto e vírgula e JSON, implementado com o padrão Adapter em um projeto Maven separado, publicado no GitHub e incluído como dependência via JitPack.

#### Scenario: CSV
- **WHEN** o formato CSV está ativo
- **THEN** cada registro é uma linha com campos separados por ";"

#### Scenario: Dependência
- **WHEN** o projeto principal é compilado em uma máquina limpa
- **THEN** o Maven baixa o módulo de log pelo JitPack sem passos manuais

### Requirement: Tela de Configuração
O sistema SHALL oferecer uma tela de Configuração para escolher o formato do log, aplicar a troca em tempo de execução e persistir a escolha.

#### Scenario: Troca em execução
- **WHEN** o usuário troca de CSV para JSON
- **THEN** a próxima operação já é registrada em JSON, sem reiniciar
- **AND** a escolha continua valendo após reiniciar o sistema
