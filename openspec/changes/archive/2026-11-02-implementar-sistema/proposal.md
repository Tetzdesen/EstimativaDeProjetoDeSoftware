# Proposal

## Why

O repositório já tem um sistema Swing MDI parcialmente pronto em `com.br.estimativadeprojetodesoftware` (login, cadastro, janelas, CRUDs iniciais), mas o núcleo de domínio não atende às 10 specs de `openspec/specs/`: perfis guardam um valor por campo em vez de um valor por plataforma, "não aplicável" é gravado como 0, o cálculo usa `double`/`int` e divide o Nível de UI pela quantidade de perfis, só existe SQLite e o log grava CSV com vírgula e sem o nome do projeto. Esta change leva o código existente à conformidade com todas as specs (casos de uso, BR01 a BR12 e RNF01 a RNF09) em um único `/opsx:apply`, reaproveitando o que já funciona.

## What Changes

- **Mantido e ajustado**: casca MDI (`Principal`, `GlobalWindowManager`, `window_command`, Memento), Login e Cadastro de usuário com hash PBKDF2, validador de senhas do JitPack (passa a ficar atrás de um Adapter), esqueletos de State, Command e Observer, adapter de PDF (PDFBox) e a fachada `LogService` (casos de uso Fazer Login e Criar Novo Usuário; RNF04, RNF09).
- **Reescrito**: modelo de perfil e projeto por plataforma com "não aplicável" distinto de zero, schema do banco, seeder do perfil padrão a partir de `perfil-padrao.csv` (idempotente) e repositórios JDBC (BR01, BR04; RNF02).
- **Novo**: suporte a H2 ao lado do SQLite, escolhido no `.env` pela Abstract Factory existente (RNF02, RNF03).
- **Reescrito**: motor de cálculo em `BigDecimal` idêntico à planilha da Tabela 1, com Nível de UI pela média de 3 colunas, custos adicionais extensíveis, impostos, lucro, meses e média mensal (BR02, BR03, BR07, BR09, BR11, BR12).
- **Ajustado**: CRUD de projetos com State + Command (Visualização, Inclusão, Edição e Somente leitura) e Prototype para a cópia de perfis (BR03, BR08; RNF01, RNF05).
- **Ajustado**: compartilhamento somente leitura com "Compartilhado por NOME" (BR05) e exportação em PDF e CSV com o relatório completo (BR06, BR10).
- **BREAKING**: o log deixa de usar `com.github.Tetzdesen:loggingR` e passa a usar o novo módulo `estimativa-log` (projeto Maven separado, Adapter para CSV com ";" e JSON, publicado via JitPack), com o texto exato da spec `log-sistema` e uma tela de Configuração (RNF07, RNF08).
- **BREAKING**: o schema antigo (`campo`, `perfil_has_campo`, `projeto_has_campo` etc.) é substituído; bancos de desenvolvimento existentes (`db/database.db`) precisam ser apagados.
- **Removido**: Builders de perfil por plataforma, Chain de cálculo, `Campo`/`CampoService`/`CampoRepository`, `EstimaProjetoService`, `RegistroOperacao` e seeders antigos, substituídos pelo novo núcleo.
- Observer entre janelas abertas (RNF06).

Fora do escopo:
- Alterar qualquer requisito das specs.
- Renomear o pacote base `com.br.estimativadeprojetodesoftware`.
- Trocar o `.env` por outro formato de configuração.
- Versão web ou mobile, Docker, Hibernate, JPA ou outro framework de persistência.
- Migrar dados de bancos criados com o schema antigo.
- Novos formatos de exportação além de PDF e CSV (a arquitetura só precisa permiti-los).

## Capabilities

### New Capabilities
Nenhuma. As capacidades já existem em `openspec/specs/`.

### Modified Capabilities
Nenhuma. Esta change só implementa os requisitos existentes, por isso declara `skip_specs: true`.

## Impact

- Altera o código em `src/main/java/com/br/estimativadeprojetodesoftware/`, remove as classes listadas acima e o pacote raiz `seeders/`, e altera o `pom.xml` (adiciona H2, o módulo de log e o plugin de jar executável; remove o `loggingR`).
- Cria a pasta `estimativa-log/` (módulo de log independente, com Gson).
- Altera `.env.example` (opção `H2` e sua fábrica) e o `README.md`.
- Gera `docs/requisitos-implementados.md`, base do Google Docs exigido na entrega.
