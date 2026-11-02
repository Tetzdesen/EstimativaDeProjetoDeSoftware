# interface Specification

## Purpose
Definir a interface Swing MDI, a arquitetura MVP Passive View e a sincronização entre janelas (RNF04, RNF06).

## Requirements

### Requirement: MDI
O sistema SHALL usar Java Swing com MDI, permitindo várias janelas internas abertas e navegação entre elas.

#### Scenario: Janelas simultâneas
- **WHEN** o usuário abre a lista de projetos e um perfil
- **THEN** as duas janelas ficam abertas dentro da janela principal

#### Scenario: Rodapé
- **WHEN** há um usuário autenticado
- **THEN** o rodapé da janela principal exibe o nome desse usuário

### Requirement: Atualização entre janelas
Janelas abertas que exibem o mesmo dado SHALL ser atualizadas quando esse dado mudar em outra janela, usando o padrão Observer.

#### Scenario: Renomear projeto
- **WHEN** o usuário renomeia um projeto na janela de edição
- **THEN** a lista de projetos aberta mostra o novo nome sem ser reaberta

### Requirement: Passive View
Views SHALL NOT conter regra de negócio nem acesso a dados; toda lógica SHALL ficar nos presenters e serviços.

#### Scenario: Revisão de código
- **WHEN** uma classe de view é inspecionada
- **THEN** ela só expõe componentes e métodos de exibição chamados pelo presenter
