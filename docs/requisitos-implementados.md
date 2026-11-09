# Requisitos implementados

Situação de cada caso de uso, regra de negócio (BR) e requisito não funcional (RNF) do Trabalho Final, com o local no código. Os caminhos de classes partem de `src/main/java/com/br/estimativadeprojetodesoftware/`.

**Legenda:** ✅ atendido totalmente · ⚠️ atendido parcialmente (o motivo está na coluna de observações).

## Casos de uso

| Caso de uso | Situação | Onde está |
|---|---|---|
| Fazer Login | ✅ | `presenter/usuario/LoginPresenter`, `command/usuario/RealizarAutenticacaoUsuarioCommand`, `service/AutenticacaoService`, `service/SenhaHashService` (PBKDF2). Credenciais inválidas geram sempre "E-mail ou senha inválidos.". |
| Criar Novo Usuário | ✅ | `presenter/usuario/CadastroUsuarioPresenter`, `service/UsuarioService.cadastrar` (e-mail único, senha validada pelo Adapter). |
| Manter Perfis de Projeto de Estimativa | ✅ | `presenter/perfil/ListaPerfisPresenter`, `presenter/perfil/ManterPerfilPresenter`, `state/perfil/*`, `command/perfil/*`, `service/PerfilService`. |
| Criar Projeto de Estimativa | ✅ | `state/projeto/InclusaoProjetoState`, `command/projeto/CriarProjetoCommand`, `service/ProjetoService.criar`, `model/Projeto.aPartirDePerfis` + `model/Perfil.copiarParaProjeto` (Prototype). |
| Manter Projetos de Estimativa | ✅ | `presenter/projeto/ManterProjetoPresenter`, `presenter/projeto/ListaProjetosPresenter`, `state/projeto/*`, `command/projeto/*`, `service/ProjetoService`. |
| Elaborar Estimativa | ✅ | `service/CalculoEstimativaService`, `model/ResultadoEstimativa`, tela de projeto em seções escolhidas numa barra lateral (`view/projeto/ProjetoView`, `view/projeto/SecaoProjeto`, `presenter/tabela/NavegacaoProjetoModel`, com contadores de selecionadas por categoria), seção "Tamanho e UI" com botões de opção (`view/componentes/RenderizadorOpcao`), barra de resumo no rodapé (`view/componentes/BarraResumo`) e seção "Detalhamento"; recálculo a cada alteração em `command/projeto/AtualizarEstimativaCommand`; busca e "Somente selecionadas" (`presenter/tabela/FiltroFuncionalidades`). |
| Compartilhar Projeto de Estimativa | ✅ | `presenter/projeto/CompartilharProjetoPresenter`, `service/CompartilhamentoService`, `state/projeto/SomenteLeituraProjetoState`. |
| Exportar Projeto de Estimativa | ✅ | `service/ExportacaoService`, `templatemethod/ExportadorProjeto`, `ExportarPDFTemplate`, `ExportarCSVTemplate`, `ExportadorFactory`, `adapter/exportarprojeto/PDFExportAdapter`. |

## Regras de negócio

| Código | Situação | Onde está | Observações |
|---|---|---|---|
| BR01 | ✅ | `seeder/PerfilPadraoSeeder`, `src/main/resources/seed/perfil-padrao.csv` | Perfil padrão do EstimateMyApp carregado na primeira execução, em SQLite, H2 ou PostgreSQL, sem duplicar. |
| BR02 | ✅ | `service/CalculoEstimativaService` | Plataformas marcadas de forma independente; exige ao menos uma. |
| BR03 | ✅ | `model/Projeto.selecionar`, `CalculoEstimativaService` | Tamanho e Nível de UI de escolha única; demais categorias múltiplas; dias digitados em Desenvolvimento Específico; Gerente opcional. |
| BR04 | ✅ | `service/PerfilService.excluir` | CRUD de perfis com nova funcionalidade e alteração de prazos; perfil padrão protegido contra exclusão, com o motivo informado. |
| BR05 | ✅ | `service/CompartilhamentoService`, `SomenteLeituraProjetoState`, `ListaProjetosPresenter` | Destinatário só visualiza; lista mostra "Compartilhado por NOME"; vê sempre a versão atual (Observer recarrega a janela aberta). |
| BR06 | ✅ | `service/ExportacaoService.verificarPodeExportar` | Só exporta projeto calculado; destinatário de compartilhamento não exporta. |
| BR07 | ✅ | `CalculoEstimativaService` | Nível de UI pela fórmula da planilha: soma dos dias-base × (soma dos percentuais ÷ 3). |
| BR08 | ✅ | `model/Perfil.copiarParaProjeto`, `model/Funcionalidade.copiar` | Projeto é cópia profunda: alterar o projeto não muda o perfil nem outros projetos. |
| BR09 | ✅ | `CalculoEstimativaService` | Custo por papel (desenvolvimento, design, gerência) com taxa por plataforma. |
| BR10 | ✅ | `templatemethod/*` | Relatório PDF e CSV com funcionalidades selecionadas, dias e custos por plataforma, resumo por categoria, custos adicionais, impostos, lucro, prazo e preço final. Novo formato = nova subclasse registrada no `ExportadorFactory`. |
| BR11 | ✅ | `model/CustoAdicional.padroes`, `presenter/tabela/CustosTableModel` | Lista editável iniciada com os 6 itens; valores alteráveis, zeráveis e novos itens. |
| BR12 | ✅ | `CalculoEstimativaService`, `model/ResultadoEstimativa` | Custo total, impostos, lucro, preço final, prazo em meses e média mensal; impostos e lucro configuráveis por projeto. |

## Requisitos não funcionais

| Código | Situação | Onde está | Observações |
|---|---|---|---|
| RNF01 | ⚠️ | `state/projeto/*`, `state/perfil/*`, `command/projeto/*`, `command/perfil/*` | State integrado a Command: cada estado habilita só as ações válidas e executa cada ação por um Command. Os estados (Visualização, Inclusão, Edição e Somente leitura) seguem o padrão visto em aula, mas ainda não foram comparados com o diagrama do Whimsical. |
| RNF02 | ✅ | `singleton/ConexaoSingleton`, `singleton/ConexaoReconectavel`, `strategy/SQLiteInitializer`, `strategy/H2Initializer`, `strategy/PostgresInitializer`, `abstractfactory/FabricaRepositoryH2`, `abstractfactory/FabricaRepositoryPostgres`, `migracao/MigradorBanco` | SQLite, H2 ou PostgreSQL (Supabase) trocando só `TIPO_BANCO`, `DB_URL` e credenciais no `.env`; sem Docker. PostgreSQL só com SSL verificado, reconexão automática e migração de dados entre bancos (`docs/supabase/configurar.sql` para preparar o Supabase). |
| RNF03 | ✅ | `repository/*`, `repository/jdbc/*`, `abstractfactory/*` | Repository com JDBC puro (sem Hibernate/JPA); falhas viram `RepositorioException` com mensagem clara exibida sem fechar o sistema. |
| RNF04 | ✅ | `view/projeto/PrincipalProjetoView`, `presenter/helpers/Navegador` | Swing MDI (`JDesktopPane` + `JInternalFrame`), várias janelas abertas, rodapé com o usuário logado. |
| RNF05 | ✅ | `presenter/*`, `view/*` | MVP Passive View: as views só expõem componentes; regras ficam em presenters e services. |
| RNF06 | ✅ | `observer/*`, `service/ServicoObservavel`, presenters de lista e de edição | Observer: renomear um projeto atualiza a lista aberta e a árvore sem reabrir. |
| RNF07 | ✅ | `estimativa-log/` (publicado em github.com/Tetzdesen/estimativa-log), `service/LogService` | Adapter com CSV (separador `;`) e JSON (Gson) em projeto Maven separado, com o texto exato da spec. Publicado no GitHub com a tag `1.1.0` (Java 25) e baixado pelo JitPack (`com.github.Tetzdesen:estimativa-log:1.1.0`), sem passos manuais. |
| RNF08 | ✅ | `view/configuracao/ConfiguracaoView`, `presenter/configuracao/ConfiguracaoPresenter`, tabela `configuracao` | Troca de formato em tempo de execução, mantida após reiniciar. |
| RNF09 | ✅ | `adapter/senha/ValidadorSenhaAdapter` | Validador `com.github.claytonfraga:validadorsenha` (JitPack) atrás de um Adapter; mensagens do validador exibidas no cadastro. |

## Dashboard (complemento à interface)

| Item | Situação | Onde está | Observações |
|---|---|---|---|
| Dashboard do usuário | ✅ | `service/DashboardService`, `model/ResumoDashboard`, `presenter/dashboard/DashboardPresenter`, `view/dashboard/DashboardView` | Abre após o login e pela barra, pelo menu Sistema e pelo nó "Principal". Mostra quantidade de projetos próprios, compartilhados e calculados, prazo total e preço final total (só projetos próprios calculados), gráfico de preço por projeto e de dias por plataforma (JFreeChart). Atualiza via Observer quando projetos mudam ou o usuário troca. |

## Aparência (complemento à interface)

| Item | Situação | Onde está | Observações |
|---|---|---|---|
| Tema claro e escuro | ✅ | `view/tema/TemaClaro`, `TemaEscuro` (+ `.properties`), `command/AplicarTemaCommand`, `service/PreferenciasService`, `presenter/configuracao/ConfiguracaoPresenter` | FlatLaf 3.5.4 com paleta própria. A troca na tela de Configuração vale na hora em todas as janelas (gráficos remontados via Observer, evento `TEMA`) e fica salva na tabela `configuracao` (`interface.tema`). |
| Padrão visual consistente | ✅ | `view/componentes/*` (`JanelaInterna`, `BarraAcoes`, `Formulario`, `TabelaPadrao`, `CartaoIndicador`, `BarraResumo`, `Selo`, `PainelCartao`), `service/IconService`, `src/main/resources/icones/*.svg` | Ícones SVG próprios que acompanham a cor do tema. Tabelas com linhas alternadas, números à direita, reais em "R$ 1.234,56" e "NA" em itálico, distinto de zero. Resultado da estimativa em tabelas e na barra de resumo da tela de projeto; cartões no dashboard. |
| Mensagens padronizadas | ✅ | `command/ExibirMensagemCommand`, `ConfirmarCommand`, `PerguntarCommand`, `view/componentes/Notificacao`, `Dialogos` | Sucesso como aviso que some sozinho; aviso e erro em diálogo. Exclusões e saída do sistema pedem confirmação com botões "Excluir"/"Sair" e "Cancelar" (Esc cancela). |
| Retorno visual em formulários | ✅ | Presenters de login, cadastro, usuário, perfil e projeto; `presenter/helpers/ValidacaoVisual` | Campo destacado com a mensagem do problema, que some ao corrigir. |
| Atalhos de teclado | ✅ | `JanelaInterna`, `ManterUsuarioView`, `PrincipalProjetoPresenter` | Enter, Esc, Ctrl+S (respeita o estado da tela) e Ctrl+N. |

## Log de operações

Criação, alteração, exclusão, compartilhamento e exportação de projeto geram:

- sucesso: `criação: Loja, (02/11/2026, 14:30:00, e Ana)`
- falha: `Ocorreu a falha <mensagem> ao realizar a exclusão do projeto Loja, (02/11/2026, 14:30:00, e Ana).`

## Verificação

- `mvn clean package`: 70 testes passando (inclui os casos de aceitação do dashboard, a persistência do tema, os ícones e os renderizadores das tabelas), entre eles o caso de aceitação da planilha (240,67 dias, R$ 109.966,67 de desenvolvimento, R$ 212.597,67 de preço final, 241 dias, 8,0 meses e R$ 26.501,09 de média mensal) e a persistência em SQLite e H2.
- `estimativa-log/`: 8 testes passando, para os formatos CSV e JSON; build da tag `1.1.0` no JitPack com JDK 25.
- Smoke test da interface rodado sobre o jar, com SQLite e com H2, nos temas claro e escuro. Ele cobriu cadastro, login, perfil novo, criação de projeto, cálculo, renomear com atualização da lista, compartilhamento em somente leitura, troca do formato de log e exportação em PDF e CSV.
