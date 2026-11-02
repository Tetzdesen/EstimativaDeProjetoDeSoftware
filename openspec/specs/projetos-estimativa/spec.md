# projetos-estimativa Specification

## Purpose
Criar e manter projetos de estimativa a partir de perfis (casos de uso Criar Projeto de Estimativa e Manter Projetos de Estimativa; BR03, BR08; RNF01, RNF05).

## Requirements

### Requirement: Criar projeto a partir de perfis
O sistema SHALL criar um projeto como cópia independente de um ou mais perfis selecionados. Alterações no projeto SHALL NOT afetar os perfis nem outros projetos.

#### Scenario: Isolamento da cópia
- **WHEN** o usuário altera os dias de "Calendário" no projeto
- **THEN** o perfil de origem e os demais projetos mantêm o valor original

#### Scenario: Vários perfis
- **WHEN** o usuário seleciona dois perfis com funcionalidades diferentes
- **THEN** o projeto contém as funcionalidades dos dois

#### Scenario: Registro em log
- **WHEN** o projeto é criado
- **THEN** a operação é registrada no log

### Requirement: Manter projetos com State e Command
O CRUD de projetos SHALL seguir os estados de navegação definidos em aula (diagrama do Whimsical do RNF01), e cada ação disponível em um estado SHALL ser um Command.

#### Scenario: Ações por estado
- **WHEN** a janela está em modo de visualização
- **THEN** apenas as ações válidas nesse estado ficam habilitadas

#### Scenario: Alteração registrada
- **WHEN** o dono salva uma alteração no projeto
- **THEN** a alteração é persistida e registrada no log

#### Scenario: Exclusão confirmada
- **WHEN** o dono confirma a exclusão de um projeto
- **THEN** o projeto é removido e a operação é registrada no log

### Requirement: Personalização no projeto
O sistema SHALL permitir personalizar no projeto qualquer métrica: dias, percentuais, taxas por plataforma, custos adicionais, impostos e lucro.

#### Scenario: Taxa diferente por plataforma
- **WHEN** o usuário define taxa de design R$ 550,00 apenas para iOS e Android
- **THEN** o cálculo usa R$ 550,00 nessas plataformas e mantém a taxa da Web
