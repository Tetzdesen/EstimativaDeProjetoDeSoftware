# dashboard Specification

## Purpose

Dar ao usuário logado uma visão consolidada dos seus projetos de estimativa, com indicadores de quantidade, prazo e preço e dois gráficos, sempre com os dados atuais.

## Requirements

### Requirement: Abertura do dashboard
O sistema SHALL abrir o dashboard como janela interna da janela principal logo após o login, e SHALL permitir reabri-lo pela barra de ferramentas, pelo menu Sistema e pelo nó "Principal" da árvore de navegação. Se o dashboard já estiver aberto, a ação SHALL trazê-lo para a frente em vez de abrir outro.

#### Scenario: Após o login
- **WHEN** o usuário faz login com credenciais válidas
- **THEN** a janela principal MDI abre com o dashboard visível dentro dela

#### Scenario: Reabrir sem duplicar
- **WHEN** o dashboard já está aberto e o usuário clica em "Dashboard" na barra de ferramentas
- **THEN** a janela existente vai para a frente e continua havendo um único dashboard aberto

### Requirement: Indicadores do usuário
O dashboard SHALL exibir, para o usuário logado: a quantidade de projetos próprios, a quantidade de projetos compartilhados com ele, a quantidade de projetos próprios com estimativa calculada, a soma dos dias e a soma dos preços finais dos projetos próprios calculados. Projetos compartilhados MUST NOT entrar nas somas de dias e de preço. Os valores SHALL ser calculados a partir do estado atual de cada projeto, com o mesmo cálculo da estimativa, e arredondados apenas na exibição: dias como inteiro e reais com duas casas.

#### Scenario: Projeto do caso de aceitação
- **WHEN** o usuário tem um único projeto, calculado, igual ao protótipo da planilha (240,67 dias e preço final de R$ 212.597,67)
- **THEN** o dashboard mostra 1 projeto próprio, 1 calculado, 241 dias e R$ 212.597,67

#### Scenario: Projeto não calculado
- **WHEN** o usuário tem dois projetos próprios e só um foi calculado
- **THEN** o dashboard mostra 2 projetos próprios e 1 calculado, e as somas de dias e de preço consideram apenas o calculado

#### Scenario: Projeto compartilhado
- **WHEN** outro usuário compartilha com ele um projeto calculado
- **THEN** a quantidade de projetos compartilhados aumenta em 1 e as somas de dias e de preço não mudam

### Requirement: Gráficos
O dashboard SHALL exibir um gráfico com o preço final de cada projeto próprio calculado e um gráfico com os dias somados por plataforma (Web/Back-end, iOS, Android) nesses mesmos projetos. Uma plataforma desmarcada em um projeto SHALL contar 0 dias nesse projeto.

#### Scenario: Preço por projeto
- **WHEN** o usuário tem dois projetos próprios calculados
- **THEN** o gráfico de preço mostra uma fatia para cada projeto, com o respectivo preço final

#### Scenario: Dias por plataforma
- **WHEN** o único projeto calculado é o protótipo da planilha (Web/Back-end e Android marcados)
- **THEN** o gráfico de plataformas mostra 129,33 dias em Web/Back-end, 0 em iOS e 111,33 em Android

### Requirement: Sem projetos calculados
Quando o usuário não tiver projeto próprio calculado, o dashboard SHALL exibir zero nas somas e uma mensagem orientando a criar um projeto e calcular a estimativa, sem exibir gráficos vazios.

#### Scenario: Usuário novo
- **WHEN** um usuário recém-cadastrado faz login
- **THEN** o dashboard mostra 0 projetos, 0 dias, R$ 0,00 e a orientação para criar e calcular um projeto

### Requirement: Atualização automática
O dashboard aberto SHALL ser atualizado, sem ser reaberto, quando um projeto do usuário for criado, alterado, calculado ou excluído, ou quando um projeto for compartilhado com ele, usando o padrão Observer. Ao trocar de usuário logado, o dashboard SHALL passar a mostrar os dados do novo usuário.

#### Scenario: Cálculo em outra janela
- **WHEN** o dashboard está aberto e o usuário calcula um projeto na janela de estimativa
- **THEN** o dashboard passa a mostrar esse projeto nos indicadores e nos gráficos

#### Scenario: Exclusão
- **WHEN** o usuário exclui um projeto calculado
- **THEN** as somas de dias e de preço do dashboard diminuem sem reabrir a janela
