## Context

O código atual (`com.br.estimativadeprojetodesoftware`) já tem tudo de que o dashboard precisa:
- `ProjetoService.listarProprios` e `listarCompartilhadosComigo`, que devolvem projetos completos;
- `CalculoEstimativaService.calcular`, que devolve `ResultadoEstimativa` com dias e custo por plataforma e o preço final;
- o Observer compartilhado (`PublicadorEventos`, `EventoDominio`), que avisa sobre mudanças em PROJETO, COMPARTILHAMENTO e SESSAO;
- o `Navegador`, que abre janelas internas por chave sem duplicar;
- a dependência `org.jfree:jfreechart:1.5.3` no `pom.xml`, hoje sem uso.

O dashboard antigo foi removido no commit `5cc3959` e serve só de referência visual: três indicadores e dois gráficos de pizza.

## Goals / Non-Goals

**Goals:**
- Mostrar os indicadores e os gráficos da spec `dashboard` com valores reais, consistentes com a tela de estimativa.
- Manter MVP Passive View: a view recebe dados prontos e só os exibe.
- Deixar a consolidação testável sem interface gráfica.

**Non-Goals:**
- Guardar o resultado do cálculo no banco (os valores continuam sendo calculados na hora).
- Mudar as telas existentes além dos pontos de abertura do dashboard.

## Decisions

### Classes novas e padrão de cada uma

| Classe | Pacote | Papel | Padrão |
|---|---|---|---|
| `ResumoDashboard` | `model` | Valores consolidados: contagens, soma de dias, soma de preços, preço por projeto e dias por plataforma, sem arredondar | — (objeto de valor) |
| `DashboardService` | `service` | Monta o `ResumoDashboard` do usuário a partir de `ProjetoService` e `CalculoEstimativaService` | Recebe repositórios pelo `ProjetoService` (Repository) |
| `DashboardView` | `view.dashboard` | `JInternalFrame` com rótulos dos indicadores, área de gráficos e mensagem de vazio | MVP Passive View |
| `DashboardPresenter` | `presenter.dashboard` | Formata os valores, monta os datasets e os gráficos, e se registra como observador | MVP + Observer |

### Consolidação
`DashboardService.resumir(Usuario)`:
1. lista os projetos próprios e os compartilhados;
2. para cada projeto próprio com `isCalculado()`, chama `calcular(projeto)` e acumula o dia total, o preço final e os dias por plataforma (ausente = 0);
3. um projeto calculado que falhe na validação, por exemplo sem plataforma marcada após uma edição, é ignorado nas somas e contado à parte (`projetosComErro`), para o dashboard não quebrar.

Esse é o mesmo cálculo da tela de estimativa, então os valores batem com ela.

**Alternativa considerada:** gravar o resultado no projeto ao calcular. Foi descartada porque exigiria mudar o banco e ficaria desatualizada quando o dono altera o projeto sem recalcular.

### Gráficos
- Preço por projeto: pizza (`DefaultPieDataset`), como no dashboard antigo.
- Dias por plataforma: barras (`DefaultCategoryDataset`), porque a pizza esconde a plataforma com 0 dias, que a spec exige mostrar.

Os datasets são montados no presenter e os `ChartPanel` são entregues à view por `exibirGraficos(JComponent, JComponent)`. A view não conhece o JFreeChart. Quando não há projeto calculado, o presenter chama `exibirMensagemVazia(String)`.

### Atualização (Observer)
O `DashboardPresenter` se registra no publicador compartilhado e reage a eventos de PROJETO, COMPARTILHAMENTO e SESSAO. O recálculo roda dentro de `SwingUtilities.invokeLater`. O observador é removido quando a janela fecha. Na troca de usuário, que gera um evento SESSAO, o presenter lê o novo usuário do `UsuarioLogadoSingleton`.

### Pontos de abertura
- `Navegador.abrirDashboard()`, com a chave "dashboard".
- `PrincipalProjetoPresenter`:
  - novo comando "Dashboard", com botão na barra e item no menu Sistema;
  - o nó "Principal" da árvore passa a abrir o dashboard;
  - ao abrir a janela principal, abre o dashboard no lugar da lista de projetos.

## Risks / Trade-offs

- **Recalcular todos os projetos a cada evento pode ficar lento com muitos projetos** → o cálculo é em memória e leva milissegundos por projeto; aceitável para o volume de um trabalho acadêmico. Se ficar lento, agrupar eventos próximos.
- **Projeto marcado como calculado, mas inválido no estado atual** → ignorado nas somas e contado à parte, sem quebrar a janela.
- **JFreeChart em teste automatizado** → o teste cobre o `DashboardService`, que não depende de interface. A view é conferida executando o sistema.
