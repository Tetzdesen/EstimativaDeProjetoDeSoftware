# compartilhamento Specification

## Purpose
Permitir que outros usuários registrados consultem um projeto sem alterá-lo (BR05; caso de uso Compartilhar Projeto de Estimativa).

## Requirements

### Requirement: Compartilhar projeto
O dono SHALL poder compartilhar um projeto com usuários registrados. O destinatário SHALL apenas visualizar o projeto, sem editar, excluir ou exportar.

#### Scenario: Indicação de origem
- **WHEN** o destinatário abre a lista de projetos
- **THEN** o projeto aparece com a indicação "Compartilhado por NOME_DO_USUARIO"

#### Scenario: Ações bloqueadas
- **WHEN** o destinatário abre o projeto compartilhado
- **THEN** editar, excluir e exportar ficam indisponíveis

#### Scenario: Versão atual
- **WHEN** o dono altera um projeto já compartilhado
- **THEN** o destinatário passa a ver a versão alterada

#### Scenario: Registro em log
- **WHEN** o compartilhamento é concluído
- **THEN** a operação é registrada no log
