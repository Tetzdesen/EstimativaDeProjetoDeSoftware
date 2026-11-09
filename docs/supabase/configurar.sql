-- Configuração do banco do sistema no Supabase.
-- Rodar no SQL Editor do projeto, conectado como postgres.
--
-- Antes de executar, troque TROCAR_PELA_SENHA_DO_APP pela senha do papel do
-- sistema (a mesma que vai em DB_PASSWORD no .env local). NÃO salve este arquivo
-- com a senha: ela só deve existir no editor e no .env, que fica fora do git.

-- 1. Papel exclusivo do sistema: só faz login e só enxerga o esquema estimativa.
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'estimativa_app') THEN
    CREATE ROLE estimativa_app LOGIN PASSWORD 'TROCAR_PELA_SENHA_DO_APP'
      NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOREPLICATION NOBYPASSRLS;
  ELSE
    -- os demais atributos só podem ser alterados por superusuário; já foram definidos na criação
    ALTER ROLE estimativa_app LOGIN PASSWORD 'TROCAR_PELA_SENHA_DO_APP';
  END IF;
END
$$;

-- No PostgreSQL 16+, quem cria um papel não pode agir como ele sem esta concessão;
-- ela é necessária para o postgres passar o esquema ao papel do sistema.
GRANT estimativa_app TO postgres;

-- 2. Esquema próprio, fora dos esquemas expostos pela Data API (public, graphql_public).
--    O papel do sistema é o dono: o sistema cria as tabelas que faltam a cada início.
CREATE SCHEMA IF NOT EXISTS estimativa AUTHORIZATION estimativa_app;
ALTER SCHEMA estimativa OWNER TO estimativa_app;
ALTER ROLE estimativa_app SET search_path = estimativa;

-- 3. Ninguém além do dono usa o esquema.
REVOKE ALL ON SCHEMA estimativa FROM PUBLIC;
REVOKE ALL ON SCHEMA estimativa FROM anon, authenticated;
REVOKE ALL ON ALL TABLES IN SCHEMA estimativa FROM PUBLIC, anon, authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE estimativa_app IN SCHEMA estimativa
  REVOKE ALL ON TABLES FROM PUBLIC, anon, authenticated;

-- 4. O papel do sistema não cria nada fora do próprio esquema.
REVOKE CREATE ON SCHEMA public FROM estimativa_app;

-- 5. RLS em todas as tabelas do esquema, sem políticas (nega tudo à API).
--    O sistema também ativa o RLS ao criar as tabelas; este bloco serve para
--    conferir ou corrigir depois de uma migração.
DO $$
DECLARE
  t record;
BEGIN
  FOR t IN SELECT tablename FROM pg_tables WHERE schemaname = 'estimativa' LOOP
    EXECUTE format('ALTER TABLE estimativa.%I ENABLE ROW LEVEL SECURITY', t.tablename);
  END LOOP;
END
$$;

-- 6. Conferência: todas as tabelas devem aparecer com rls = true e sem permissões para anon.
SELECT c.relname AS tabela, c.relrowsecurity AS rls,
       has_table_privilege('anon', c.oid, 'SELECT') AS anon_le
FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
WHERE n.nspname = 'estimativa' AND c.relkind = 'r'
ORDER BY 1;
