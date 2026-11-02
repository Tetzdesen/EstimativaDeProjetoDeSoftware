## Why

A interface funciona, mas não tem aparência profissional. Cada tela foi montada de um jeito: `FlowLayout` e `GridLayout` sem espaçamento padrão, botões de tamanhos diferentes, ícones PNG coloridos de estilos e tamanhos misturados (32 px, 64 px e 512 px), mensagens em `JOptionPane` genérico e tabelas sem formatação. A janela de login e as telas de usuário ainda seguem o visual anterior. O FlatLaf já está no projeto (versão 3.1, tema claro), mas sem tema próprio. Esta change dá ao sistema um padrão visual único, moderno e consistente dentro do Swing, com tema claro e escuro. Ela reforça a interface MDI (RNF04), mantém a arquitetura MVP Passive View (RNF05) e não altera nenhuma regra de negócio.

## What Changes

- Tema FlatLaf atualizado e personalizado: paleta própria com cor de destaque, cantos arredondados, fontes e espaçamentos padronizados.
- **Tema claro e escuro**, escolhido na tela de Configuração, aplicado na hora em todas as janelas abertas (inclusive nos gráficos do dashboard) e salvo para os próximos inícios.
- Conjunto único de ícones vetoriais (SVG) com o mesmo traço e tamanho, que acompanham a cor do tema. Substitui os PNGs atuais.
- Componentes visuais reutilizáveis para todas as telas: cabeçalho de janela, barra de ações, formulário em grade alinhada, cartões de indicador e tabelas com cabeçalho, zebra, alinhamento numérico e "NA" destacado.
- Mensagens padronizadas por tipo (sucesso, aviso, erro e confirmação), substituindo os `JOptionPane` soltos. Ações destrutivas sempre pedem confirmação.
- Retorno visual em formulários: campo obrigatório vazio ou valor inválido destacado em vermelho, com a mensagem ao lado.
- Atalhos de teclado: Enter confirma, Esc cancela ou fecha, Ctrl+S salva nas telas de edição e Ctrl+N cria novo projeto na janela principal.
- Telas refeitas com o novo padrão: login, cadastro de usuário, manter usuário, janela principal (barra, árvore, rodapé), dashboard, listas de projetos e perfis, perfil, projeto/estimativa (incluindo um resultado em formato de tabela em vez de texto), compartilhamento e configuração.
- Remoção dos recursos visuais que deixam de ser usados (PNGs, `MensagemModalView`, `CustomTableRenderer`, `AbsoluteLayout`).

Fora do escopo:
- Mudar regras de negócio, cálculo, persistência, log ou o fluxo entre telas.
- Trocar o Swing por outra tecnologia (JavaFX, web).
- Internacionalização ou escolha de idioma.
- Temas além de claro e escuro, ou personalização de cores pelo usuário.

## Capabilities

### New Capabilities
- `aparencia`: tema claro/escuro com troca em execução e persistência, padrão visual consistente (ícones, tabelas, formulários), mensagens padronizadas com confirmação de ações destrutivas, destaque de erros em formulários e atalhos de teclado.

### Modified Capabilities
Nenhuma. As specs `interface` (MDI, Observer, Passive View) e `dashboard` continuam valendo sem mudança de requisito; esta change só altera a forma como as telas aparecem.

## Impact

- **Código:**
  - Todas as classes em `view/`.
  - Os presenters, na parte de preenchimento visual, mensagens e atalhos.
  - `SetLookAndFeelCommand`, `IconService`, `BarraService` e `MostrarMensagemCommand`.
  - Um novo pacote `view.componentes`, com os componentes reutilizáveis.
- **Recursos:** novos ícones SVG e um arquivo de propriedades do tema em `src/main/resources/`; os PNGs atuais são removidos.
- **Dependências:**
  - FlatLaf atualizado de 3.1 para 3.5.4.
  - Adição de `flatlaf-extras`, para ícones SVG.
  - Remoção de `org.netbeans.external:AbsoluteLayout`.
- **Banco:** nova chave `interface.tema` na tabela `configuracao` (sem mudança de esquema).
