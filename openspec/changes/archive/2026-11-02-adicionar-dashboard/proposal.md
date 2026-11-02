## Why

O sistema anterior tinha um dashboard ("Principal") com totais e gráficos dos projetos do usuário. Ele foi removido na change `implementar-sistema` porque dependia do modelo antigo e, mesmo antes, mostrava custo sempre igual a zero (o cálculo estava comentado). Hoje, ao entrar no sistema, o usuário vê só a lista de projetos e não tem uma visão consolidada de prazo e preço. Esta change traz o dashboard de volta, calculado sobre o modelo atual e com valores reais. É um complemento à interface MDI (RNF04) e usa o padrão Observer entre janelas (RNF06); não altera nenhuma BR.

## What Changes

- Nova janela interna **Dashboard**, aberta automaticamente após o login e acessível pela barra de ferramentas, pelo menu Sistema e pelo nó "Principal" da árvore.
- Indicadores do usuário logado: quantidade de projetos próprios, de projetos compartilhados com ele e de projetos próprios já calculados; soma dos dias e soma dos preços finais dos projetos próprios calculados.
- Dois gráficos: preço final por projeto (projetos próprios calculados) e dias por plataforma (Web/Back-end, iOS, Android) somados nesses projetos.
- Mensagem orientando o usuário quando ainda não há projeto calculado.
- Atualização automática do dashboard aberto quando um projeto é criado, alterado, calculado, excluído ou compartilhado (Observer).
- Valores calculados na hora, a partir do estado atual de cada projeto, com o mesmo cálculo e o mesmo arredondamento de exibição da tela de estimativa.

Fora do escopo:
- Alterar o cálculo, as regras de negócio ou as demais telas.
- Incluir projetos compartilhados nos totais financeiros: eles só entram na contagem, porque o preço é do dono.
- Filtros, períodos, exportação do dashboard ou gráficos além dos dois descritos.
- Dashboard de perfis ou de outros usuários.

## Capabilities

### New Capabilities
- `dashboard`: visão consolidada dos projetos do usuário logado (indicadores e gráficos), aberta após o login e atualizada automaticamente.

### Modified Capabilities
Nenhuma. A interface MDI e o Observer (spec `interface`) já cobrem janelas simultâneas e atualização entre janelas; o dashboard só passa a usá-los.

## Impact

- Novos: view e presenter do dashboard, um service que consolida os indicadores e testes desse service.
- Alterados: a janela principal (abrir o dashboard após o login, botão na barra, item de menu e nó "Principal" da árvore) e o navegador de janelas.
- Dependência: usa o JFreeChart, que já está no `pom.xml`; nenhuma dependência nova.
- Sem mudança no banco de dados.
