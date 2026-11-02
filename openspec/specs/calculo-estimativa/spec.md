# calculo-estimativa Specification

## Purpose
Calcular dias e custo de desenvolvimento conforme a planilha da Tabela 1 (BR02, BR03, BR07, BR09; caso de uso Elaborar Estimativa). Por decisão do professor, o cálculo segue a planilha.

## Requirements

### Requirement: Seleção de plataformas
O sistema SHALL permitir marcar de forma independente Web/Back-end, iOS e Android, exigindo ao menos uma plataforma.

#### Scenario: Multiplataforma sem back-end
- **WHEN** o usuário marca iOS e Android e desmarca Web/Back-end
- **THEN** o cálculo soma apenas as colunas iOS e Android

#### Scenario: Nenhuma plataforma
- **WHEN** nenhuma plataforma está marcada
- **THEN** o sistema não calcula e informa o motivo

### Requirement: Tipos de seleção por categoria
Tamanho do App e Nível de UI SHALL ser de escolha única. As demais categorias SHALL ser de múltipla escolha. "Desenvolvimento Específico do Aplicativo" SHALL aceitar dias digitados. "Gerente de Projeto" SHALL ser opcional.

#### Scenario: Troca de tamanho
- **WHEN** o usuário seleciona "Grande" com "Pequeno" já selecionado
- **THEN** "Pequeno" é desmarcado

### Requirement: Contagem separada por plataforma
Uma funcionalidade selecionada SHALL contar uma vez em cada plataforma marcada onde for aplicável, sem eliminar duplicidade entre front-end e back-end.

#### Scenario: Cadastro em duas plataformas
- **WHEN** "Cadastro por E-mail e Senha" (1 dia) é selecionado com Web/Back-end e Android marcados
- **THEN** o total soma 2 dias

### Requirement: Nível de UI percentual
Os dias de UI SHALL seguir a fórmula da Tabela 1: (soma dos dias de Tamanho do App das plataformas marcadas) × (soma dos percentuais de UI das plataformas marcadas ÷ 3). Plataformas desmarcadas SHALL contar como 0% na média. No detalhamento por plataforma, cada plataforma marcada recebe seus dias-base × (soma dos percentuais ÷ 3).

#### Scenario: Uma plataforma
- **WHEN** apenas uma plataforma está marcada, com tamanho Pequeno (10 dias) e UI Profissional (70%)
- **THEN** são acrescentados 2,33 dias de UI (10 × 70% ÷ 3)

#### Scenario: Duas plataformas
- **WHEN** Web/Back-end e Android estão marcados com tamanho Grande (50 dias cada) e UI Profissional
- **THEN** são acrescentados 46,67 dias de UI (23,33 por plataforma)

#### Scenario: Três plataformas
- **WHEN** as três plataformas estão marcadas com tamanho Pequeno (10 dias cada) e UI Profissional
- **THEN** são acrescentados 21 dias de UI (7 por plataforma)

### Requirement: Custo por papel
O custo de desenvolvimento SHALL ser a soma, por plataforma, de: dias de tamanho, funcionalidades e desenvolvimento específico × taxa de desenvolvimento; dias de UI × taxa de design; dias de gerência × taxa de gerência. O prazo total SHALL ser a soma de todos os dias.

#### Scenario: Caso de aceitação (protótipo da Tabela 1)
- **WHEN** Web/Back-end e Android estão marcados, com tamanho Grande, UI Profissional, Gerente de Projeto e as funcionalidades do protótipo da planilha (cadastro por e-mail, Facebook e Twitter; e-mails de convite; contas multi-tenant; dashboard; e-mails transacionais; tags; pesquisa de texto livre; fóruns; suporte multilíngue; SSL; proteção contra DoS; autenticação em duas etapas; QR Code no Android)
- **AND** as taxas são desenvolvimento R$ 450,00, design R$ 550,00 e gerência R$ 300,00
- **THEN** Web/Back-end soma 129,33 dias e R$ 59.033,33
- **AND** Android soma 111,33 dias e R$ 50.933,33
- **AND** o total de desenvolvimento é 240,67 dias e R$ 109.966,67

### Requirement: Precisão numérica
Dias e valores SHALL ser calculados em `BigDecimal` sem arredondamento intermediário, com arredondamento apenas na exibição, como na planilha: total de dias inteiro, meses com uma casa decimal e valores em reais com duas casas.

#### Scenario: Soma consistente
- **WHEN** o projeto tem dias fracionados de UI
- **THEN** o valor total corresponde à soma exata das linhas, sem erro acumulado de arredondamento
