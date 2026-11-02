## 1. Base do tema

- [x] 1.1 Atualizar o `pom.xml`: FlatLaf 3.5.4, `flatlaf-extras` 3.5.4 e remoção do `AbsoluteLayout`; verificar com `mvn -q compile` e `mvn -q test`
- [x] 1.2 Criar `model/Tema` (CLARO, ESCURO) e `service/PreferenciasService` (lê/grava `interface.tema` na tabela `configuracao`; ausente ou inválido = CLARO); verificar com teste em H2 (padrão claro, gravar escuro, reler após "reiniciar" com novo service)
- [x] 1.3 Criar `view/tema/TemaClaro` e `TemaEscuro` com seus `.properties` (cor de destaque, arcos, fonte, tabela, cores `Estimativa.sucesso/aviso/erro/na`); verificar abrindo uma janela de teste nos dois temas e conferindo por screenshot
- [x] 1.4 Criar `AplicarTemaCommand` (FlatLaf.setup + updateUI + evento `TEMA`; acrescentar `TEMA` em `EventoDominio.Entidade`), substituir `SetLookAndFeelCommand` e aplicar o tema salvo no `Principal` antes do login (com falha no banco caindo no claro); verificar executando: o login abre no tema gravado

## 2. Ícones

- [x] 2.1 Desenhar o conjunto de cerca de 22 ícones SVG em `src/main/resources/icones/` (viewBox 24, traço 1,75, cor única `#6E6E6E`); verificar por screenshot de uma grade com todos os ícones nos dois temas
- [x] 2.2 Reescrever o `IconService` com `FlatSVGIcon` + `ColorFilter` ligado ao `UIManager` (`getIcon(chave)` 18 px e `getIcon(chave, tamanho)`), trocar as chaves usadas no código e remover os PNGs; verificar com `mvn -q compile` e um teste que carrega todas as chaves sem erro

## 3. Componentes reutilizáveis (`view.componentes`)

- [x] 3.1 Criar `JanelaInterna` (cabeçalho com ícone/título/subtítulo, área de conteúdo, Esc fecha, Ctrl+S clica em Salvar) e `BarraAcoes` (botão principal destacado, separadores); verificar com `mvn -q compile`
- [x] 3.2 Criar `Formulario` (grade alinhada, rótulo de erro por campo, `mostrarErro`/`limparErros` com outline de erro, placeholder) e `CartaoIndicador`; verificar com `mvn -q compile`
- [x] 3.3 Criar `TabelaPadrao` com renderizadores (número à direita, reais "R$ 1.234,56", percentual, "NA" em itálico com `Estimativa.na`); verificar com teste dos renderizadores (texto e alinhamento de "NA", "0", "R$ 59.033,33")
- [x] 3.4 Criar `Notificacao` (aviso não modal que some em 3 s) e `Dialogos` (erro, aviso, confirmação com botões próprios, entrada de texto; Esc cancela); verificar executando: notificação some sozinha e Esc cancela a confirmação

## 4. Mensagens padronizadas

- [x] 4.1 Criar `TipoMensagem`, o novo `ExibirMensagemCommand` (sucesso via `Notificacao`, aviso/erro via `Dialogos`, com ponto de troca para testes) e `ConfirmarCommand`; remover `MostrarMensagemCommand`, o `ExibirMensagemCommand` antigo, `MensagemModalView` e `MensagemModalPresenter`; verificar com `mvn -q compile`
- [x] 4.2 Trocar todas as mensagens e `JOptionPane` dos presenters, states e views pelos novos commands, com o tipo certo (sucesso/aviso/erro) e confirmação em excluir projeto, excluir perfil, excluir conta e sair; verificar com `grep` (nenhum `JOptionPane` fora de `Dialogos`) e executando uma exclusão cancelada com Esc

## 5. Telas de usuário

- [x] 5.1 Refazer `PrincipalUsuarioView` e `LoginView` (cartão central, logo, `Formulario`, Enter confirma, link para criar conta) e validação visual no `LoginPresenter`; verificar executando: login vazio destaca os campos e Enter entra
- [x] 5.2 Refazer `CadastroUsuarioView` e a validação visual no `CadastroUsuarioPresenter` (obrigatórios, e-mail, senhas iguais, mensagens do validador e e-mail duplicado no campo); verificar executando o cenário "Cadastro incompleto" da spec
- [x] 5.3 Refazer `ManterUsuarioView` (sem GroupLayout gerado, com `Formulario` e `BarraAcoes`) mantendo os estados atuais; verificar executando editar e salvar o usuário

## 6. Janela principal e dashboard

- [x] 6.1 Refazer `PrincipalProjetoView`/`BarraService` (barra com grupos e ícones SVG, árvore com ícones, rodapé com usuário, banco e formato do log) e Ctrl+N para novo projeto; verificar executando
- [x] 6.2 Refazer `DashboardView` com `JanelaInterna` + `CartaoIndicador` e aplicar cores do `UIManager` nos gráficos; o `DashboardPresenter` remonta os gráficos ao receber o evento `TEMA`; verificar executando a troca de tema com o dashboard aberto (screenshot nos dois temas)

## 7. Listas, perfil, compartilhamento e configuração

- [x] 7.1 Refazer `ListaProjetosView` e `ListaPerfisView` com `JanelaInterna` + `TabelaPadrao` (coluna de origem com "Meu projeto"/"Compartilhado por NOME" e status); verificar executando
- [x] 7.2 Refazer `ManterPerfilView` (nome em `Formulario`, abas Funcionalidades/Taxas, `TabelaPadrao` com combos, "NA" destacado) e validação visual do nome; verificar executando criar e editar um perfil
- [x] 7.3 Refazer `CompartilharProjetoView` com `JanelaInterna` e listas estilizadas; verificar executando um compartilhamento
- [x] 7.4 Refazer `ConfiguracaoView` com as seções "Aparência" (Claro/Escuro) e "Log" (CSV/JSON); o `ConfiguracaoPresenter` grava o tema e executa `AplicarTemaCommand`; verificar executando os cenários "Troca em execução" e "Escolha mantida" da spec

## 8. Tela de projeto

- [x] 8.1 Refazer o topo e as abas de `ProjetoView` (nome, plataformas em botões de alternância, selo de situação, abas Funcionalidades/Taxas/Custos com `TabelaPadrao`) mantendo os estados e commands; verificar executando os fluxos de inclusão, edição e somente leitura
- [x] 8.2 Substituir o resultado em texto pela aba Resultado com `CartaoIndicador` e três `TabelaPadrao` (por plataforma, por categoria, resumo financeiro) preenchidas pelo `ManterProjetoPresenter`; verificar com o caso de aceitação: cartões mostram 241 dias, 8,0 meses, R$ 212.597,67 e R$ 26.501,09
- [x] 8.3 Validação visual dos campos de impostos, lucro e nome no `ManterProjetoPresenter` antes de calcular ou salvar; verificar executando o cenário "Percentual inválido" da spec

## 9. Verificação final

- [x] 9.1 Revisar: nenhuma cor fixa (`new Color`/`Color.X`) em views e renderizadores fora dos temas, nenhum `JOptionPane` fora de `Dialogos`, views sem regra de negócio
- [x] 9.2 Atualizar os smoke tests da interface (fora do repositório) e executá-los com SQLite e H2 nos dois temas, com screenshots de login, dashboard, projeto e configuração
- [x] 9.3 `mvn clean package` sem erros e todos os testes passando
- [x] 9.4 Atualizar `docs/requisitos-implementados.md` (seção de aparência) e o `README.md` (tema claro/escuro e atalhos)
