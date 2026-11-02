## Context

Estado atual da interface (pacote `com.br.estimativadeprojetodesoftware`):
- **Look and feel:** `SetLookAndFeelCommand` aplica `FlatLightLaf` (FlatLaf 3.1) sem nenhuma personalização.
- **Views:** 14 views. As de usuário (`LoginView`, `CadastroUsuarioView`, `ManterUsuarioView`, `PrincipalUsuarioView`) vêm da versão antiga e usam `GridBagLayout`/`GroupLayout` com tamanhos fixos. As demais usam `FlowLayout`/`GridLayout` sem espaçamento padrão.
- **Ícones:** `IconService` carrega 18 PNGs coloridos de 32 px, 64 px e até 512 px, que ficam ruins no fundo escuro.
- **Mensagens:** `MostrarMensagemCommand` (com um ponto de troca usado pelos smoke tests) e `JOptionPane` espalhados em 4 presenters, 1 state e 1 view.
- **Resultado da estimativa:** texto monoespaçado num `JTextArea`.
- **Dashboard:** usa JFreeChart com as cores padrão da biblioteca.
- **Configuração:** a tela de Configuração e a tabela `configuracao` (chave/valor) já existem e guardam o formato do log.

Restrições do projeto: Swing puro, MVP Passive View (a view não decide nada), padrões já usados (State + Command, Observer, Adapter) e o software tem que compilar e rodar em máquina limpa.

## Goals / Non-Goals

**Goals:**
- Um único padrão visual, construído com componentes reutilizáveis e não repetido tela a tela.
- Tema claro e escuro trocando em execução, inclusive nos gráficos e nos renderizadores próprios.
- Manter os presenters responsáveis pelas decisões (validação, qual mensagem mostrar, estado dos botões), com a view só exibindo.

**Non-Goals:**
- Animações, transições ou componentes de terceiros além do FlatLaf.
- Mudar o fluxo entre telas, os estados ou os commands existentes.

## Decisions

### 1. FlatLaf com tema próprio (claro e escuro)
- **Dependências:** `com.formdev:flatlaf` passa para 3.5.4; entram `flatlaf-extras` 3.5.4 (ícones SVG) e o JFreeChart, que já existe.
- **Classes de tema:** em `view.tema`, `TemaClaro extends FlatLightLaf` e `TemaEscuro extends FlatDarkLaf`. Cada uma tem um `.properties` de mesmo nome, que o FlatLaf carrega automaticamente, com:
  - a cor de destaque (`@accentColor`);
  - `Component.arc`/`Button.arc` em 8;
  - fonte base 13 pt;
  - `Table.alternateRowColor`, `Table.showHorizontalLines` e `Table.rowHeight` em 26;
  - `TitlePane.unifiedBackground`;
  - as cores semânticas do sistema: `Estimativa.sucesso`, `Estimativa.aviso`, `Estimativa.erro` e `Estimativa.na`.
- **Por que não os temas IntelliJ prontos:** cores próprias dão identidade e controle do contraste dos valores "NA" e das mensagens.

### 2. Escolha e aplicação do tema
- `enum Tema { CLARO, ESCURO }` em `model`.
- `PreferenciasService` (service) lê e grava a chave `interface.tema` em `IConfiguracaoRepository`. Valor ausente ou inválido vale `CLARO`. Esse service é testável sem interface gráfica.
- `AplicarTemaCommand` (command, substitui `SetLookAndFeelCommand`):
  - executa `FlatLaf.setup(...)` com a classe do tema e depois `FlatLaf.updateUI()`, que atualiza todas as janelas abertas;
  - publica `EventoDominio(ALTERACAO, Entidade.TEMA, null)` no `PublicadorEventos`.
- **Observer:** o `DashboardPresenter` reage a `TEMA` e remonta os gráficos com as cores do `UIManager`. Renderizadores e componentes próprios leem as cores do `UIManager` a cada pintura, então acompanham o tema sem código extra.
- **Inicialização:** o `Principal` abre a conexão e aplica o tema salvo antes de mostrar o login. Se o banco falhar, usa o tema claro, e o erro de conexão continua aparecendo no login como hoje.
- **Configuração:** a tela ganha a seção "Aparência" (rádios Claro/Escuro). O `ConfiguracaoPresenter` grava pelo `PreferenciasService` e executa o `AplicarTemaCommand`.

### 3. Ícones SVG
- `IconService.getIcon(chave)` passa a devolver `FlatSVGIcon` de `/icones/<chave>.svg` em 18 px. `getIcon(chave, tamanho)` cobre 24 e 32 px (árvore, cabeçalho e login).
- **Desenho:** os SVGs são autorais, com traço de 1,75 px no estilo contorno e viewBox 24. Cada um usa uma única cor neutra (`#6E6E6E`), que um `ColorFilter` global troca por `Label.foreground`. Assim o ícone acompanha o tema. Os ícones de ação primária usam a cor de destaque.
- **Conjunto (cerca de 22):** dashboard, projeto, novo-projeto, perfil, compartilhar, configuracao, usuario, sair, salvar, editar, excluir, cancelar, calcular, exportar-pdf, exportar-csv, adicionar, remover, abrir, fechar, olho, olho-fechado, logo.
- Os PNGs de `src/main/resources/` são removidos.

### 4. Componentes reutilizáveis (`view.componentes`)
Todos são só de exibição: não acessam service nem tomam decisão.

| Componente | Papel |
|---|---|
| `JanelaInterna` | Base dos `JInternalFrame`: cabeçalho (ícone, título, subtítulo), `BarraAcoes` no topo e área de conteúdo com margem de 16 px. Registra Esc para fechar e Ctrl+S para clicar em Salvar (`doClick` num botão desabilitado não dispara, o que respeita o estado). |
| `BarraAcoes` | Botões com ícone e texto, alinhados à esquerda; `adicionarPrincipal(...)` destaca o botão (estilo default do FlatLaf); separadores entre grupos. |
| `Formulario` | Grade de rótulo + campo com `GridBagLayout` e espaçamento fixo; para cada campo, um rótulo de erro. `mostrarErro(campo, msg)` aplica `FlatClientProperties.OUTLINE = "error"` e mostra a mensagem; `limparErros()`. Placeholder via `JTextField.placeholderText`. |
| `CartaoIndicador` | Título pequeno + valor grande (dashboard e resultado). |
| `TabelaPadrao` | `JTable` com cabeçalho em negrito, zebra, altura de linha padrão, ordenação e renderizadores: número à direita, reais "R$ 1.234,56", percentual, e "NA" em itálico com a cor `Estimativa.na`. |
| `Notificacao` | Aviso não modal no canto inferior direito da janela principal (`JLayeredPane`), com cor por tipo, que some após 3 s (`javax.swing.Timer`). |
| `Dialogos` | Erro, aviso e confirmação com textos de botão próprios ("Excluir"/"Cancelar"); Esc cancela. Construído sobre `JOptionPane`, que o FlatLaf já estiliza. |

### 5. Mensagens (presenter → view)
- `MostrarMensagemCommand` vira `ExibirMensagemCommand(TipoMensagem, texto)` (com sucesso, aviso e erro). O exibidor padrão usa `Notificacao` para sucesso e `Dialogos` para aviso e erro. O ponto de troca para testes é mantido.
- `ConfirmarCommand` devolve a resposta. Os `JOptionPane` espalhados (listas, perfil, `VisualizacaoUsuarioState`, `ProjetoView.confirmar/perguntar`) passam a usar esses commands. A view só oferece `escolherArquivo` e o diálogo de entrada de texto do custo adicional, este também padronizado em `Dialogos`.

### 6. Validação visual nos formulários
- O presenter valida antes de chamar o service: campos obrigatórios, e-mail, número e percentual. Para isso usa os métodos de exibição da view (`mostrarErro(campo, msg)`, `limparErros()`).
- O destaque some quando o campo muda, por um `DocumentListener` registrado pelo presenter.
- O service continua validando (defesa em profundidade); erros que só ele detecta, como e-mail duplicado, também são mostrados no campo correspondente.

### 7. Telas
- **Login e cadastro:** cartão central com logo, título, `Formulario`, botão principal largo e link "Criar conta". Enter confirma.
- **Janela principal:**
  - barra de ferramentas com ícones de 18 px e grupos separados;
  - árvore com ícones SVG e linha selecionada destacada;
  - rodapé com usuário, banco em uso (SQLite/H2) e formato do log;
  - Ctrl+N abre um novo projeto.
- **Listas (projetos e perfis):** `JanelaInterna` + `TabelaPadrao`. A lista de projetos ganha uma coluna de origem com selo ("Meu projeto" ou "Compartilhado por NOME") e uma coluna de status.
- **Perfil:** `Formulario` para o nome; abas "Funcionalidades" e "Taxas"; `TabelaPadrao` com combos estilizados.
- **Projeto:**
  - cabeçalho com nome, plataformas em botões de alternância e selo de situação ("Editando", "Calculado", "Somente leitura");
  - abas Funcionalidades, Taxas e Custos;
  - aba **Resultado**, com cartões (prazo em dias e meses, custo de desenvolvimento, preço final, média mensal) e três tabelas (por plataforma, por categoria e resumo financeiro), no lugar do texto monoespaçado.
- **Dashboard:** `CartaoIndicador` e gráficos com fundo, grade e fontes vindos do `UIManager`.
- **Compartilhar, Configuração e Usuário:** `JanelaInterna` ou diálogo com `Formulario`.

### 8. Remoções
- `MensagemModalView`, `MensagemModalPresenter`, `ExibirMensagemCommand` antigo e `CustomTableRenderer`, que não são usados.
- Os PNGs.
- A dependência `AbsoluteLayout`, sem uso.

### Padrões por classe nova
| Classe | Padrão |
|---|---|
| `TemaClaro`, `TemaEscuro` | Strategy, com o tema escolhido em tempo de execução |
| `PreferenciasService` | Repository, via `IConfiguracaoRepository` |
| `AplicarTemaCommand`, `ExibirMensagemCommand`, `ConfirmarCommand` | Command |
| `DashboardPresenter` ao reagir a `TEMA` | Observer |
| Componentes de `view.componentes` | Passive View (só exibição) |

## Risks / Trade-offs

- **Gráficos do JFreeChart não acompanham `updateUI()`** → o `DashboardPresenter` observa o evento `TEMA` e remonta os gráficos.
- **Renderizadores com cor fixa ficam ilegíveis no escuro** → toda cor vem do `UIManager` (chaves `Estimativa.*`), nunca de `Color` fixo. Coberto por revisão na tarefa final.
- **Ícones SVG autorais podem ficar irregulares** → grade de 24 px, mesmo traço e conferência visual nos dois temas por screenshots.
- **Abrir o banco antes do login muda a ordem de inicialização** → em falha, usa o tema claro e mantém a mensagem de erro no login. Os smoke tests já existentes cobrem a abertura.
- **Volume grande de telas** → os componentes reutilizáveis vêm primeiro, e cada tela é uma tarefa separada que se verifica executando o sistema.
- **FlatLaf 3.1 → 3.5.4** → atualização sem quebra de API pública. Compilar e rodar os testes logo após a troca.
