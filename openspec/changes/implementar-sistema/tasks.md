# Tasks

## 1. Preparação

- [x] 1.1 Atualizar o `pom.xml`: adicionar H2 e o plugin de jar executável com dependências (main `principal.Principal`), manter o `exec-maven-plugin`; verificar com `mvn -q compile`
- [x] 1.2 Copiar `openspec/reference/perfil-padrao.csv` para `src/main/resources/seed/` e verificar que o arquivo está no classpath após `mvn -q compile` (`target/classes/seed/`)
- [x] 1.3 Adicionar ao `.env.example` a opção `H2` (`TIPO_BANCO`, `DB_URL` comentada e chave `H2` com a fábrica) e `LOG_ARQUIVO`; verificar que o `.env.example` copiado para `.env` continua iniciando o sistema em SQLite

## 2. Módulo de log (projeto Maven separado)

- [x] 2.1 Criar `estimativa-log/` com `pom.xml` próprio (groupId `com.github.Tetzdesen`, artifactId `estimativa-log`, versão 1.0.0, Java 17, Gson, JUnit 5) e `jitpack.yml` com JDK 17; verificar com `mvn -q compile` dentro de `estimativa-log/`
- [x] 2.2 Implementar `Logger`, `LogRegistro` (operação, nome do projeto, data DD/MM/AAAA, hora HH:MM:SS, usuário, mensagem de falha opcional) e `LoggerFactory`; verificar com teste dos textos de sucesso e falha da spec `log-sistema`
- [x] 2.3 Implementar `CsvLoggerAdapter` (campos separados por `;`) e `JsonLoggerAdapter` (Gson); verificar com testes que gravam em arquivo temporário e conferem o separador `;` e o JSON válido
- [x] 2.4 Rodar `mvn install` em `estimativa-log/`, trocar no projeto principal a dependência `loggingR` pela `estimativa-log` e verificar com `mvn -q compile` (as classes que usam `com.br.log` ainda podem ser ajustadas na tarefa 5.3)

## 3. Persistência

- [x] 3.1 Generalizar `ConexaoSingleton` para escolher o `DatabaseInitializer` por `TIPO_BANCO` (`SQLite`, `H2`), criar `H2Initializer` e `FabricaRepositoryH2`, e rodar DDL e seeders a cada início (não só com banco novo); verificar iniciando com cada valor de `TIPO_BANCO`
- [x] 3.2 Escrever o DDL novo do `design.md` (`CREATE TABLE IF NOT EXISTS`, compatível com os dois bancos) nos initializers, substituindo `CriarTabelasSQLiteCommand`; verificar que as tabelas existem em SQLite e H2
- [x] 3.3 Criar as entidades novas de `model` (`Perfil`, `Funcionalidade`, `ValorPlataforma`, `TaxaDiaria`, `ItemProjeto`, `CustoAdicional`, `ResultadoEstimativa`) e os enums `Plataforma`, `Papel`, `TipoSelecao`, `TipoValor`; reescrever `Projeto`; verificar com testes de unidade de NA ≠ 0 e de validações básicas
- [x] 3.4 Criar `RepositorioException` e as interfaces `IPerfilRepository`, `IProjetoRepository`, `ICompartilhamentoRepository`, `IConfiguracaoRepository`; adaptar `FabricaRepository`; verificar com `mvn -q compile`
- [x] 3.5 Implementar `JdbcUsuarioRepository` (a partir de `UsuarioRepositorySQLite`, sem a coluna `log`) e `JdbcConfiguracaoRepository`; verificar com teste de integração em H2 em memória
- [x] 3.6 Implementar `JdbcPerfilRepository` (perfil, funcionalidades, valores por plataforma com nulo = NA, taxas); verificar com teste de ida e volta em H2 em memória
- [x] 3.7 Implementar `JdbcProjetoRepository` (projeto, cópia de funcionalidades e valores, taxas, custos adicionais) e `JdbcCompartilhamentoRepository`; verificar com teste de ida e volta em H2 em memória
- [x] 3.8 Criar `PerfilPadraoSeeder` que lê o CSV ("NA" vira nulo) e insere o perfil padrão com taxas desenvolvimento R$ 450,00, design R$ 450,00 e gerência R$ 300,00, sem duplicar; mover o seeder de admin para `seeder/UsuarioSeeder`; verificar com teste rodando o seeder duas vezes
- [x] 3.9 Teste de integração: schema, seeder e leitura do perfil padrão (todas as funcionalidades do CSV; "Planos de Assinatura" NA em iOS; "Relatório de Erros" 0 em Web) em SQLite (arquivo temporário) e em H2 (em memória)

## 4. Autenticação

- [x] 4.1 Criar `IValidadorSenha` e `ValidadorSenhaAdapter` envolvendo `com.pss.senha.validacao.ValidadorSenha`, substituir `ValidadorSenhaService` nos presenters de cadastro; verificar com teste de senha fraca (mensagens do validador) e senha forte
- [x] 4.2 Ajustar `AutenticacaoService`/cadastro ao novo `IUsuarioRepository`: e-mail único com mensagem de motivo e mensagem genérica no login inválido; verificar com testes de e-mail duplicado e credenciais inválidas
- [x] 4.3 Conferir que após o login a janela MDI abre e o rodapé mostra o nome do usuário; verificar executando `mvn exec:java`

## 5. Infraestrutura de UI (Observer, State, Command, Log)

- [x] 5.1 Fazer `update()` do Observer receber um `EventoDominio` (tipo e id); `PerfilService`, `ProjetoService` e `CompartilhamentoService` notificam criação, alteração e exclusão; verificar com teste de um observador falso
- [x] 5.2 Criar `ManterProjetoPresenterState` com `VisualizacaoProjetoState`, `InclusaoProjetoState`, `EdicaoProjetoState` e `SomenteLeituraProjetoState`, cada um habilitando só as ações válidas e disparando um Command por ação; remover `EstimadoState`/`NaoEstimadoState`; verificar com `mvn -q compile`
- [x] 5.3 Reescrever `LogService` sobre o `Logger` do módulo `estimativa-log`, com o formato lido de `IConfiguracaoRepository` e troca em tempo de execução; remover `RegistroOperacao` e os usos de `com.br.log`; verificar com teste que troca CSV → JSON e confere o arquivo gerado

## 6. Perfis

- [x] 6.1 Criar `PerfilService` (substitui `PerfilProjetoService`) com CRUD completo e bloqueio de exclusão do perfil padrão com motivo; verificar com testes de criar perfil personalizado e de excluir perfil padrão
- [x] 6.2 Ajustar as telas de lista e edição de perfil ao modelo novo (funcionalidades por categoria, valores por plataforma, "não aplicável", taxas por plataforma e papel), mantendo os estados de perfil existentes com Commands; verificar criando um perfil pela tela
- [x] 6.3 Listas abertas se atualizam via Observer quando um perfil muda; verificar com as duas janelas abertas

## 7. Projetos

- [x] 7.1 Implementar `Perfil.copiarParaProjeto()` (Prototype, cópia profunda) e a criação de projeto a partir de um ou mais perfis; verificar com teste de dois perfis com funcionalidades diferentes
- [x] 7.2 Reescrever `ProjetoService` com CRUD restrito ao dono e log de criação, alteração e exclusão (sucesso e falha); verificar com teste usando um `Logger` falso
- [x] 7.3 Ajustar a lista de projetos (próprios e "Compartilhado por NOME") e a tela de cadastro com seleção de perfis; renomear um projeto atualiza a lista aberta via Observer; verificar executando o sistema
- [x] 7.4 Teste: alterar os dias de "Calendário" no projeto não altera o perfil de origem nem outros projetos

## 8. Cálculo e preço

- [x] 8.1 Implementar `CalculoEstimativaService` exatamente como o algoritmo do `design.md`, retornando `ResultadoEstimativa` com dias e custos por plataforma, por categoria e totais; remover `chain/*`, `EstimaProjetoService`, `builder/*`, `Campo*` e `seeders/`; verificar com `mvn -q compile`
- [x] 8.2 Validações: ao menos uma plataforma; escolha única em Tamanho e Nível de UI; itens não aplicáveis não selecionáveis; verificar com testes de unidade
- [x] 8.3 Custos adicionais editáveis e extensíveis (itens iniciais da BR11), impostos e lucro configuráveis por projeto; verificar com testes de item novo e item zerado
- [x] 8.4 Reescrever a tela de detalhe/estimativa: checkboxes de plataforma, seleções por categoria, dias específicos, gerente, taxas por plataforma, custos adicionais, impostos, lucro e painel de resultado; botão Calcular marca o projeto como calculado; verificar executando o sistema
- [x] 8.5 Teste do caso de aceitação da planilha: Web 129,33 dias / R$ 59.033,33; Android 111,33 dias / R$ 50.933,33; total 240,67 dias / R$ 109.966,67; custo total R$ 115.166,67; imposto R$ 48.370,00; com impostos R$ 163.536,67; lucro R$ 49.061,00; preço final R$ 212.597,67; 241 dias; 8,0 meses; média R$ 26.501,09
- [x] 8.6 Testes dos cenários de UI: uma plataforma (2,33 dias), duas (46,67), três (21); contagem de funcionalidade em duas plataformas (2 dias); taxa de design R$ 550,00 só em iOS e Android

## 9. Compartilhamento

- [x] 9.1 `CompartilhamentoService` sobre a tabela `compartilhamento`: dono compartilha com usuários registrados; log da operação; recalcular ou cancelar não desfaz o compartilhamento; verificar com teste
- [x] 9.2 Ajustar a tela de compartilhamento; destinatário abre o projeto em `SomenteLeituraProjetoState` (editar, excluir e exportar desabilitados), vê a versão atual e o service recusa as mesmas ações; verificar com dois usuários

## 10. Exportação

- [x] 10.1 Ajustar `ExportadorProjeto` para receber projeto, `ResultadoEstimativa` e caminho (escolha do arquivo passa para view/presenter); criar `ExportadorFactory`; verificar com `mvn -q compile`
- [x] 10.2 Relatório em PDF e CSV com funcionalidades selecionadas, dias e custos por plataforma, resumo por categoria, custos adicionais, impostos, lucro, prazo e preço final; verificar com teste que gera os dois arquivos do caso de aceitação e confere os valores
- [x] 10.3 Bloquear no service a exportação de projeto não calculado (com mensagem) e de projeto compartilhado; log da operação (sucesso e falha); verificar com testes

## 11. Configuração

- [x] 11.1 Criar a tela de Configuração (view, presenter, item de menu) para escolher CSV ou JSON, substituindo o rádio do menu Log; a troca vale na próxima operação e fica salva na tabela `configuracao`; remover a coluna `log` de `usuario`; verificar trocando o formato e reiniciando

## 12. Verificação final

- [x] 12.1 Revisar todas as views: nenhuma regra de negócio ou acesso a dados (só `IconService` para ícones é aceito)
- [x] 12.2 `mvn clean package` sem erros e todos os testes passando
- [x] 12.3 Apagar `db/`, executar o jar com SQLite e depois com H2 (só trocando o `.env`) e percorrer: cadastro, login, criar projeto, calcular, compartilhar, exportar PDF e CSV, trocar formato do log
- [x] 12.4 Gerar `docs/requisitos-implementados.md` listando cada caso de uso, BR e RNF com o status (atendido totalmente ou não) e onde está no código
- [x] 12.5 Atualizar o `README.md` com como compilar, executar, trocar de banco (`.env`), apagar o banco antigo e os nomes completos da equipe

## Workflow follow-up

- Criar um repositório público no GitHub só com o conteúdo de `estimativa-log/`, criar a tag `1.0.0` e conferir o build em jitpack.io
- Trocar a dependência local do log pela coordenada `com.github.Tetzdesen:estimativa-log:1.0.0` e rodar `mvn clean package` de novo
- Copiar `docs/requisitos-implementados.md` para o Google Docs da entrega
- Rodar `/opsx:archive implementar-sistema`
