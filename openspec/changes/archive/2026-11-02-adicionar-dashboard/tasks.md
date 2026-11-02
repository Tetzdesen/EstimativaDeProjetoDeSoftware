## 1. Consolidação

- [x] 1.1 Criar `model/ResumoDashboard` (contagens de projetos próprios, compartilhados, calculados e com erro; soma de dias; soma de preços; preço por projeto; dias por plataforma) e verificar com `mvn -q compile`
- [x] 1.2 Criar `service/DashboardService.resumir(Usuario)` conforme o `design.md` (só projetos próprios calculados nas somas; plataforma desmarcada = 0; projeto inválido contado à parte) e verificar com `mvn -q compile`
- [x] 1.3 Testes do `DashboardService` em H2 em memória com os casos de aceitação da spec: o protótipo da planilha dá 1 próprio, 1 calculado, 241 dias e R$ 212.597,67, com Web 129,33 / iOS 0 / Android 111,33; um calculado e um não calculado dão 2 próprios e 1 calculado; um projeto compartilhado não entra nas somas; usuário novo dá tudo zero; projeto excluído sai das somas. Verificar com `mvn -q test`

## 2. Tela

- [x] 2.1 Criar `view/dashboard/DashboardView` (Passive View: rótulos dos indicadores, `exibirGraficos(JComponent, JComponent)` e `exibirMensagemVazia(String)`) e verificar com `mvn -q compile`
- [x] 2.2 Criar `presenter/dashboard/DashboardPresenter`: formata dias como inteiro e reais com duas casas, monta a pizza de preço por projeto e as barras de dias por plataforma, e mostra a mensagem quando não há projeto calculado. Verificar com `mvn -q compile`
- [x] 2.3 Registrar o presenter como Observer (PROJETO, COMPARTILHAMENTO, SESSAO), recalcular com `invokeLater` e remover o observador ao fechar. Verificar executando o sistema: calcular e excluir um projeto com o dashboard aberto atualiza os valores

## 3. Integração na janela principal

- [x] 3.1 Adicionar `Navegador.abrirDashboard()` (chave única) e, no `PrincipalProjetoPresenter`, o comando "Dashboard" com botão na barra e item no menu Sistema; o nó "Principal" da árvore passa a abrir o dashboard. Verificar executando: clicar de novo não abre uma segunda janela
- [x] 3.2 Abrir o dashboard automaticamente após o login, no lugar da lista de projetos. Verificar executando o login com SQLite e com H2

## 4. Verificação final

- [x] 4.1 `mvn clean package` sem erros e todos os testes passando
- [x] 4.2 Atualizar `docs/requisitos-implementados.md` (seção do dashboard) e o `README.md` (menção ao dashboard na seção de uso)
