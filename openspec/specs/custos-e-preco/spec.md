# custos-e-preco Specification

## Purpose
Compor o preço final ao cliente a partir do custo de desenvolvimento, custos adicionais, impostos e lucro (BR11, BR12).

## Requirements

### Requirement: Custos adicionais
O sistema SHALL oferecer por projeto uma lista editável de custos adicionais, iniciada com hardware e instalações físicas (R$ 1.500,00), softwares (R$ 6.000,00), riscos (R$ 1.500,00), garantia (R$ 0,00), transporte (R$ 1.000,00) e outros custos (R$ 2.000,00). O usuário SHALL poder alterar valores, zerá-los e incluir novos itens.

#### Scenario: Item novo
- **WHEN** o usuário inclui "Fundo de reserva" com R$ 2.000,00
- **THEN** o valor entra no subtotal do projeto

#### Scenario: Item zerado
- **WHEN** o usuário zera "Custo com riscos"
- **THEN** o item não altera o subtotal

### Requirement: Preço final ao cliente
O sistema SHALL calcular: Custo Total = desenvolvimento + custos adicionais; Valor com Impostos = Custo Total × (1 + percentual de impostos); Preço Final = Valor com Impostos × (1 + percentual de lucro). Impostos e lucro SHALL ser configuráveis por projeto.

#### Scenario: Caso de aceitação
- **WHEN** o desenvolvimento custa R$ 109.966,67, os custos adicionais somam R$ 5.200,00 (hardware R$ 1.200,00, softwares R$ 2.000,00, fundo de reserva R$ 2.000,00), impostos são 42% e lucro 30%
- **THEN** o custo total é R$ 115.166,67
- **AND** o imposto é R$ 48.370,00 e o valor com impostos é R$ 163.536,67
- **AND** o lucro é R$ 49.061,00 e o preço final ao cliente é R$ 212.597,67

### Requirement: Prazo em meses e média mensal
O sistema SHALL exibir o prazo em meses (dias ÷ 30) e a média mensal (preço final ÷ meses), calculados sem arredondar os dias.

#### Scenario: Caso de aceitação
- **WHEN** o projeto soma 240,67 dias e preço final de R$ 212.597,67
- **THEN** o prazo exibido é 241 dias e 8,0 meses
- **AND** a média mensal é R$ 26.501,09
