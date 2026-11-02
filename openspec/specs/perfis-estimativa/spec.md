# perfis-estimativa Specification

## Purpose
Manter os modelos de funcionalidades, dias e taxas que servem de base para os projetos (BR01, BR04; caso de uso Manter Perfis de Projeto de Estimativa).

## Requirements

### Requirement: Perfil padrão inicial
O sistema SHALL carregar no seeder o perfil padrão do EstimateMyApp descrito em `openspec/reference/perfil-padrao.csv`, com categorias, funcionalidades, dias ou percentuais por plataforma e taxas diárias.

#### Scenario: Primeira execução
- **WHEN** o sistema inicia com banco vazio, em SQLite ou H2
- **THEN** o perfil padrão existe com todas as funcionalidades do arquivo de referência
- **AND** as taxas padrão são desenvolvimento R$ 450,00, design R$ 450,00 e gerência R$ 300,00 em todas as plataformas

### Requirement: Manter perfis
O sistema SHALL permitir criar, consultar, atualizar e excluir perfis, incluindo novas funcionalidades e alteração de prazos. Perfis padrão SHALL ser protegidos contra exclusão.

#### Scenario: Criar perfil personalizado
- **WHEN** o usuário cria um perfil com uma funcionalidade nova e seus dias por plataforma
- **THEN** o perfil fica disponível como base para novos projetos

#### Scenario: Excluir perfil padrão
- **WHEN** o usuário tenta excluir um perfil padrão
- **THEN** o sistema recusa a exclusão e informa o motivo

### Requirement: Aplicabilidade por plataforma
Cada funcionalidade SHALL guardar, por plataforma, um valor em dias, um percentual ou a marcação "não aplicável". Não aplicável SHALL ser distinto de zero.

#### Scenario: Funcionalidade não aplicável
- **WHEN** a funcionalidade "Planos de Assinatura" é exibida para iOS
- **THEN** ela aparece como não aplicável, não pode ser selecionada e não soma dias

#### Scenario: Funcionalidade com zero dias
- **WHEN** "Relatório de Erros" é selecionado para Web/Back-end
- **THEN** a seleção é aceita e soma 0 dias
