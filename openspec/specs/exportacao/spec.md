# exportacao Specification

## Purpose
Gerar relatórios do projeto de estimativa (BR06, BR10; caso de uso Exportar Projeto de Estimativa).

## Requirements

### Requirement: Exportar estimativa calculada
O sistema SHALL exportar em PDF e CSV apenas projetos com estimativa calculada, e SHALL permitir adicionar novos formatos sem alterar os existentes.

#### Scenario: Projeto sem cálculo
- **WHEN** o usuário tenta exportar um projeto nunca calculado
- **THEN** o sistema recusa e informa que a estimativa precisa ser calculada

#### Scenario: Conteúdo do relatório
- **WHEN** um projeto calculado é exportado
- **THEN** o relatório contém funcionalidades selecionadas, dias e custos por plataforma, resumo por categoria, custos adicionais, percentuais de impostos e lucro, prazo e preço final

#### Scenario: Projeto compartilhado
- **WHEN** o destinatário de um projeto compartilhado tenta exportá-lo
- **THEN** a exportação não está disponível

#### Scenario: Registro em log
- **WHEN** a exportação é concluída
- **THEN** a operação é registrada no log
