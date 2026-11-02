## Purpose

Garantir uma interface Swing de aparência profissional e consistente em todas as telas, com tema claro e escuro, mensagens padronizadas, retorno visual em formulários e atalhos de teclado.

## ADDED Requirements

### Requirement: Tema claro e escuro
O sistema SHALL oferecer os temas claro e escuro, escolhidos na tela de Configuração. A troca SHALL ser aplicada imediatamente a todas as janelas abertas, sem reiniciar, e SHALL ser salva para os próximos inícios. Na primeira execução, o tema SHALL ser o claro.

#### Scenario: Troca em execução
- **WHEN** o usuário escolhe o tema escuro na tela de Configuração e salva, com a lista de projetos e o dashboard abertos
- **THEN** a janela principal, a lista, o dashboard (inclusive os gráficos) e a própria tela de Configuração passam a usar o tema escuro sem serem reabertos

#### Scenario: Escolha mantida
- **WHEN** o usuário escolhe o tema escuro, fecha o sistema e o abre de novo
- **THEN** a tela de login já abre no tema escuro

#### Scenario: Primeira execução
- **WHEN** o sistema inicia com o banco vazio
- **THEN** o tema claro é usado

### Requirement: Padrão visual consistente
Todas as telas SHALL usar o mesmo padrão visual: ícones do mesmo conjunto, com o mesmo tamanho e cor adaptada ao tema; botões de ação em uma barra no topo da janela, com o botão principal destacado; formulários com rótulos alinhados e espaçamento uniforme; e tabelas com cabeçalho, linhas alternadas, números alinhados à direita e "NA" exibido de forma distinta de zero. Valores em reais SHALL aparecer no formato "R$ 1.234,56" e percentuais com "%".

#### Scenario: Ícones uniformes
- **WHEN** o usuário alterna entre o tema claro e o escuro
- **THEN** os ícones da barra de ferramentas, da árvore e dos botões mantêm o mesmo tamanho e continuam legíveis no novo tema

#### Scenario: Tabela de funcionalidades
- **WHEN** a tela de projeto mostra "Planos de Assinatura" para iOS e "Relatório de Erros" para Web/Back-end
- **THEN** a primeira célula aparece como "NA", com estilo diferente das demais, e a segunda aparece como "0", alinhada à direita como os outros números

#### Scenario: Resultado em tabela
- **WHEN** o usuário calcula a estimativa de um projeto
- **THEN** o resultado é exibido em tabelas e cartões (por plataforma, por categoria e o resumo financeiro), com os valores formatados em reais

### Requirement: Mensagens padronizadas
O sistema SHALL exibir mensagens ao usuário em um formato único, distinguindo visualmente sucesso, aviso e erro. Mensagens de sucesso SHALL NOT exigir clique para sumir. Excluir projeto, excluir perfil, excluir a própria conta e sair do sistema SHALL pedir confirmação, com o nome do item no texto e o botão de confirmar diferente do de cancelar.

#### Scenario: Sucesso
- **WHEN** o usuário salva um projeto com sucesso
- **THEN** aparece um aviso de sucesso que some sozinho após alguns segundos, sem bloquear a janela

#### Scenario: Erro
- **WHEN** uma operação falha (por exemplo, exportar um projeto não calculado)
- **THEN** aparece uma mensagem de erro com o motivo, que permanece até o usuário fechá-la

#### Scenario: Confirmação de exclusão
- **WHEN** o usuário pede para excluir o projeto "Loja"
- **THEN** o sistema pergunta "Excluir o projeto "Loja"?" com os botões "Excluir" e "Cancelar"
- **AND** escolher "Cancelar" ou pressionar Esc não exclui nada

### Requirement: Retorno visual em formulários
Nos formulários de login, cadastro, usuário, perfil, projeto e configuração, um campo obrigatório vazio ou com valor inválido SHALL ser destacado e acompanhado da mensagem do problema quando o usuário tentar confirmar. O destaque SHALL sumir quando o campo for corrigido.

#### Scenario: Cadastro incompleto
- **WHEN** o usuário clica em Salvar no cadastro com o e-mail vazio
- **THEN** o campo de e-mail fica destacado como erro e a mensagem "Informe o e-mail." aparece junto dele
- **AND** ao digitar um e-mail válido o destaque desaparece

#### Scenario: Percentual inválido
- **WHEN** o usuário digita "abc" no campo de impostos do projeto e clica em Calcular
- **THEN** o campo fica destacado como erro, a mensagem explica o valor esperado e o cálculo não é feito

### Requirement: Atalhos de teclado
O sistema SHALL aceitar Enter para confirmar o login e os diálogos, Esc para cancelar diálogos e fechar a janela interna ativa, Ctrl+S para salvar nas telas em modo de edição ou inclusão e Ctrl+N para abrir um novo projeto a partir da janela principal. Atalhos SHALL respeitar o estado da tela: uma ação indisponível no estado atual não é executada pelo atalho.

#### Scenario: Login com Enter
- **WHEN** o usuário digita e-mail e senha e pressiona Enter
- **THEN** o login é feito como se tivesse clicado em Entrar

#### Scenario: Ctrl+S fora da edição
- **WHEN** a tela de projeto está em visualização e o usuário pressiona Ctrl+S
- **THEN** nada é salvo, porque Salvar está indisponível nesse estado

#### Scenario: Novo projeto pelo teclado
- **WHEN** o usuário pressiona Ctrl+N na janela principal
- **THEN** a tela de novo projeto é aberta
