# Design

## Context

O código existente (pacote `com.br.estimativadeprojetodesoftware`, Java 17, Maven, Swing MDI) compila, mas foi construído antes das specs. Levantamento feito em 02/11/2026:

| Área | Estado atual | Decisão |
|---|---|---|
| MDI, janelas, menu, Memento | `Principal`, `GlobalWindowManager`, `presenter/window_command` | Manter |
| Login e cadastro | Views, presenters, commands e states de usuário; PBKDF2 (`SenhaHashService`); `ValidadorSenhaService` usa o validador direto; `AutenticacaoService` usa AuthifyJava | Manter; envolver o validador num Adapter |
| Conexão e banco | `ConexaoSingleton` lê o `.env`, só SQLite, `PRAGMA` fixo, DDL só roda quando o arquivo é novo | Generalizar para SQLite e H2; DDL idempotente a cada início |
| Modelo | `PerfilProjeto` = 1 plataforma, `Map<String,Integer>`, NA gravado como 0; `Projeto` com `List<Campo>` | Reescrever |
| Seeder | Builders por plataforma e `CriarCamposSeederCommand` com valores fixos | Substituir por seeder do CSV |
| Cálculo | Chain em `double`/`int`, UI ÷ nº de perfis do usuário | Reescrever |
| Log | `loggingR` 1.0.9 (CSV com ","), `RegistroOperacao` sem nome do projeto, formato salvo em `usuario.log` | Substituir pelo módulo `estimativa-log` |
| Exportação | Template Method + `PDFExportAdapter`; relatório só com cabeçalho; `JFileChooser` dentro do template | Manter a estrutura; completar o conteúdo; mover a escolha do arquivo para view/presenter |
| Projeto (estados) | `EstimadoState`/`NaoEstimadoState`; cancelar a estimativa descompartilha | Substituir por estados de CRUD |
| Observer | `Subject`/`Observer` usados por services e presenters | Manter e completar |

O perfil padrão está em `openspec/reference/perfil-padrao.csv`.

## Goals / Non-Goals

**Goals:**
- Atender todas as specs com o sistema compilando e executando em máquina limpa com `mvn clean package`.
- Reproduzir exatamente o protótipo da planilha: 240,67 dias, R$ 109.966,67 de desenvolvimento e R$ 212.597,67 de preço final.
- Trocar SQLite por H2 apenas editando o `.env`.
- Reaproveitar a casca de UI e a autenticação existentes.

**Non-Goals:**
- Otimização de desempenho, internacionalização, empacotamento em instalador.
- Migrar dados do schema antigo.

## Decisions

### Pacotes (base `com.br.estimativadeprojetodesoftware`)

| Pacote | Conteúdo | Padrão |
|---|---|---|
| `principal` | `Principal` (main) — mantido | |
| `singleton` | `ConexaoSingleton` (generalizado), `UsuarioLogadoSingleton` — mantidos | Singleton |
| `model` | Novos: `Perfil` (substitui `PerfilProjeto`), `Funcionalidade` (categoria, nome, `TipoSelecao`, `TipoValor`, ordem), `ValorPlataforma`, `Plataforma` (WEB_BACKEND, IOS, ANDROID), `TipoSelecao` (UNICA, MULTIPLA, DIAS_DIGITADOS, OPCIONAL), `TipoValor` (DIAS, PERCENTUAL), `Papel` (DESENVOLVIMENTO, DESIGN, GERENCIA), `TaxaDiaria`, `Projeto` (reescrito), `ItemProjeto`, `CustoAdicional`, `ResultadoEstimativa`. `Usuario` mantido (sem a coluna `log`) | Prototype (`Perfil.copiarParaProjeto()`) |
| `repository` | Interfaces `IUsuarioRepository` (mantida), `IPerfilRepository`, `IProjetoRepository` (reescritas), `ICompartilhamentoRepository`, `IConfiguracaoRepository` (novas) | Repository |
| `repository.jdbc` | `Jdbc*Repository` com SQL comum aos dois bancos; tratam `SQLException` e lançam `RepositorioException` com mensagem clara | Repository |
| `abstractfactory` | `FabricaRepository`, `SeletorFabricaRepository` (mantidos), `FabricaRepositorySQLite` e `FabricaRepositoryH2` | Abstract Factory |
| `strategy` | `DatabaseInitializer`, `SQLiteInitializer` e `H2Initializer` (DDL e preparo do armazenamento por banco) | Strategy |
| `seeder` | `PerfilPadraoSeeder` (lê o CSV), `UsuarioSeeder` (admin atual) | |
| `service` | `AutenticacaoService`, `UsuarioService`, `SenhaHashService` (mantidos); `PerfilService`, `ProjetoService`, `CompartilhamentoService` (reescritos/novos, `Subject` do Observer); `CalculoEstimativaService` (novo); `ExportacaoService`; `LogService` (fachada) | Observer (Subject) |
| `adapter.senha` | `IValidadorSenha`, `ValidadorSenhaAdapter` envolvendo `com.pss.senha.validacao.ValidadorSenha` | Adapter |
| `adapter.log` | `LogService` usa `Logger` do módulo `estimativa-log`; formato lido de `IConfiguracaoRepository` | Adapter |
| `templatemethod`, `adapter.exportarprojeto` | `ExportadorProjeto` (recebe projeto, resultado e caminho), `ExportarPDFTemplate`, `ExportarCSVTemplate`, `PDFExportAdapter`, `ExportadorFactory` | Template Method, Adapter, Factory |
| `observer` | `Subject`, `Observer` (mantidos; `update()` recebe um `EventoDominio` com tipo e id) | Observer |
| `state.*` | Usuário e perfil mantidos; projeto: `ManterProjetoPresenterState` com `VisualizacaoProjetoState`, `InclusaoProjetoState`, `EdicaoProjetoState`, `SomenteLeituraProjetoState` | State |
| `command.*` | Interface `Command` mantida; um command por ação do estado (Novo, Editar, Salvar, Cancelar, Excluir, Calcular, Compartilhar, Exportar) | Command |
| `presenter.*`, `view.*` | Um presenter por tela; views só expõem componentes | MVP Passive View |

Removidos: `builder/*`, `chain/*`, `Campo`, `CampoService`, `ICampoRepository`, `repository/sqlite/*` (substituído por `repository/jdbc`), `command/seeder/*`, `command/database/CriarTabelasSQLiteCommand`, `EstimaProjetoService`, `PerfilProjetoService`, `RegistroOperacao`, `state/projeto/EstimadoState` e `NaoEstimadoState`, o pacote raiz `seeders/` e a dependência `loggingR`.

Os estados do CRUD (Visualização, Inclusão, Edição) seguem o padrão visto em aula. Se o diagrama do Whimsical do RNF01 tiver outros estados, ajustar `state.projeto` para ele. A estimativa "calculada" é um atributo do projeto (`calculado`), não um estado: ela só controla a habilitação de Exportar.

### Telas

Mantidas/ajustadas: Login, Cadastro de usuário, Principal (MDI + rodapé com usuário), Manter perfil, Lista de projetos (com "Compartilhado por NOME"), Cadastro de projeto (seleção de um ou mais perfis), Compartilhar projeto, Exportar projeto. Reescrita: Detalhe/Estimativa do projeto (plataformas, seleções por categoria, taxas por plataforma, custos adicionais, impostos, lucro, resultado). Nova: Configuração (formato do log), aberta pelo menu; o rádio CSV/JSON do menu atual é removido.

### Banco de dados

Schema novo (substitui o antigo): `usuario` (sem `log`), `perfil` (dono, nome, `padrao` booleano), `perfil_funcionalidade` (categoria, nome, tipo_selecao, tipo_valor, ordem), `perfil_valor` (funcionalidade, plataforma, valor `DECIMAL(19,6)` nulo = não aplicável), `perfil_taxa` (plataforma, papel, valor), `projeto` (dono, nome, plataformas marcadas, impostos, lucro, calculado, datas), `projeto_funcionalidade` e `projeto_valor` (cópia independente do perfil, com `selecionado` e dias digitados), `projeto_taxa`, `projeto_custo_adicional`, `compartilhamento` (projeto, usuário destinatário), `configuracao` (chave/valor; guarda o formato do log).

Ids continuam `TEXT`/`VARCHAR(36)` com UUID (como hoje), o que evita diferença de autoincremento entre os bancos. O DDL usa `CREATE TABLE IF NOT EXISTS` e roda a cada início; diferenças de dialeto ficam no `DatabaseInitializer` (`PRAGMA foreign_keys` só no SQLite). O seeder só insere o perfil padrão se não houver perfil com `padrao = true`.

`.env` (escolha do banco pela Abstract Factory existente):
```
TIPO_BANCO = "SQLite"            # ou "H2"
DB_URL = jdbc:sqlite:db/database.db   # H2: jdbc:h2:./db/estimativa
SQLite = "com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepositorySQLite"
H2 = "com.br.estimativadeprojetodesoftware.abstractfactory.FabricaRepositoryH2"
LOG_ARQUIVO = logs/estimativa
```
O `ConexaoSingleton` escolhe o `DatabaseInitializer` pelo `TIPO_BANCO` (mapa `SQLite`/`H2`). Alternativa considerada: trocar para `config.properties`; descartada para não mexer na configuração que já funciona.

### Algoritmo do cálculo (fiel à planilha)

Todos os valores em `BigDecimal` com `MathContext.DECIMAL128`; arredondar só na exibição.

```
M = plataformas marcadas (pelo menos uma)
fatorUI = (soma de pct_UI(p) para p em M) / 3
para cada p em M:
    base_p   = dias do Tamanho do App selecionado em p
    ui_p     = base_p × fatorUI
    func_p   = soma dos dias das funcionalidades selecionadas aplicáveis em p
               + dias de Desenvolvimento Específico em p
    gp_p     = (Gerente de Projeto selecionado) ? dias_gp(p) : 0
    dias_p   = base_p + ui_p + func_p + gp_p
    custo_p  = (base_p + func_p) × taxaDesenvolvimento(p)
             + ui_p × taxaDesign(p) + gp_p × taxaGerencia(p)
diasTotal       = soma dias_p
custoDev        = soma custo_p
custoTotal      = custoDev + soma custos adicionais
imposto         = custoTotal × pctImpostos
valorComImposto = custoTotal + imposto
lucro           = valorComImposto × pctLucro
precoFinal      = valorComImposto + lucro
meses           = diasTotal / 30
mediaMensal     = precoFinal / meses
```

Exibição: dias totais inteiros (HALF_UP), meses com 1 casa, reais com 2 casas. A Chain antiga é removida: ela misturava cálculo com consultas ao banco (`EstimativaFuncionalidade` instancia `CampoService`).

### Log

`estimativa-log/` é um projeto Maven independente (groupId `com.github.Tetzdesen`, artifactId `estimativa-log`, versão 1.0.0, Gson) com a interface `Logger`, `LogRegistro` (operação, nome do projeto, data DD/MM/AAAA, hora HH:MM:SS, usuário, falha opcional), `LoggerFactory` e os adaptadores `CsvLoggerAdapter` (separador `;`) e `JsonLoggerAdapter` (Gson). O `LogRegistro` gera os textos exatos da spec `log-sistema`. Durante o desenvolvimento, o projeto principal usa a versão instalada localmente (`mvn install`); na entrega, troca para a coordenada do JitPack. Alternativa considerada: corrigir o `loggingR`; descartada por decisão da equipe.

O `LogService` (fachada) recebe o formato da tabela `configuracao` e troca o `Logger` em tempo de execução quando a tela de Configuração salva.

### Compartilhamento

Tabela própria `compartilhamento` em vez de `usuario_has_projeto.isCompartilhado`. O destinatário lê sempre o projeto atual (sem cópia). O presenter abre o projeto em `SomenteLeituraProjetoState` quando o usuário logado não é o dono; `ProjetoService` também recusa editar, excluir e exportar de quem não é dono.

## Risks / Trade-offs

- **Reescrita do núcleo quebra telas existentes** → reescrever por fatia vertical (modelo → repositório → service → tela) e rodar `mvn -q compile` a cada tarefa; remover as classes antigas só quando nada mais as referencia.
- **Bancos de desenvolvimento com schema antigo** → o `README` instrui apagar `db/`; sem migração.
- **SQL comum aos dois bancos** → usar só SQL ANSI nos repositórios e cobrir SQLite e H2 em memória no teste de integração.
- **Estados do Whimsical podem diferir** → estados isolados em `state.projeto`.
- **JitPack exige repositório público e tag** → passos manuais listados em "Workflow follow-up" do `tasks.md`.
- **Fórmula de UI da planilha divide por 3 mesmo com uma plataforma** → decisão do professor; coberta por teste.
- **AuthifyJava continua como dependência externa** → mantido porque já funciona e não conflita com nenhuma spec.

## Migration Plan

1. Apagar `db/` local antes de rodar a nova versão; o schema e o seeder são recriados no primeiro início.
2. Rollback: voltar ao commit `ee9fcf0` (WIP antes do OpenSpec).
