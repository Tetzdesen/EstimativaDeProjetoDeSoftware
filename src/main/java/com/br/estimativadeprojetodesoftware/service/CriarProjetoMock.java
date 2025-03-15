package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.Campo;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CriarProjetoMock {

    private final ProjetoRepositoryMock repository;

    public CriarProjetoMock(ProjetoRepositoryMock repository) {
        this.repository = repository;
    }

    public List<Projeto> criarProjetosAleatorios(int quantidadeProjetos) {
        List<Projeto> projetosGerados = new ArrayList<>();
        for (int i = 0; i < quantidadeProjetos; i++) {
            Optional<Projeto> projeto = criarProjetoAleatorio();
            projeto.ifPresent(projetosGerados::add);
        }
        return projetosGerados;
    }

    private Optional<Projeto> criarProjetoAleatorio() {
        List<Projeto> projetosExistentes = repository.getProjetos();
        List<Perfil> perfis = new ArrayList<>();

        if (projetosExistentes.isEmpty()) {
            return Optional.empty();
        }

        Random random = new Random();
        Projeto projetoBase = projetosExistentes.get(random.nextInt(projetosExistentes.size()));

        List<String> tipos = combinarTipos(projetosExistentes, random);
        if (tipos.size() < 1 || tipos.size() > 2) {
            return Optional.empty();
        }

        String nome = gerarNomeDoProjeto(tipos);
        String criador = projetoBase.getCriador();
        String status = random.nextBoolean() ? "Estimado" : "Em andamento";
        boolean compartilhado = random.nextBoolean();
        String compartilhadoPor = compartilhado ? projetoBase.getCriador() : null;
        
        
        Perfil perfil = new Perfil("Android");
        
        
        perfis.add(perfil);

        // Criando campos para estimativa
        Map<String, Integer> tamanhosApp = perfil.getTamanhosApp();
        List<Campo> campos = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : tamanhosApp.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "tamanho", entry.getKey(), entry.getValue().doubleValue()));
        }

        // Criando campos para niveisUI
        Map<String, Double> niveisUI = perfil.getNiveisUI();
        for (Map.Entry<String, Double> entry : niveisUI.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "nivel", entry.getKey(), entry.getValue()));
        }

        // Criando campos para funcionalidades
        Map<String, Integer> funcionalidades = perfil.getFuncionalidades();
        for (Map.Entry<String, Integer> entry : funcionalidades.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "funcionalidade", entry.getKey(), entry.getValue().doubleValue()));
        }

        // Criando campos para taxas diarias
        Map<String, Double> taxasDiarias = perfil.getTaxasDiarias();

        for (Map.Entry<String, Double> entry : taxasDiarias.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "taxa diária", entry.getKey(), entry.getValue()));
        }

        List<Usuario> usuarios = new ArrayList();
        usuarios.add(UsuarioLogadoSingleton.getInstancia().getUsuario());
        return Optional.of(new Projeto(UUID.randomUUID(), nome, criador, "Android", LocalDateTime.now(), status, compartilhado, compartilhadoPor, perfis, usuarios, campos));
    }

    private String gerarNomeDoProjeto(List<String> tipos) {
        StringBuilder nomeBuilder = new StringBuilder("Projeto ");

        for (int i = 0; i < tipos.size(); i++) {
            nomeBuilder.append(tipos.get(i));
            if (i < tipos.size() - 2) {
                nomeBuilder.append(", ");
            } else if (i == tipos.size() - 2) {
                nomeBuilder.append(" e ");
            }
        }

        nomeBuilder.append(" - ").append(UUID.randomUUID().toString().substring(0, 5));
        return nomeBuilder.toString();
    }

    private List<String> combinarTipos(List<Projeto> projetos, Random random) {
        Set<String> tiposCombinados = new HashSet<>();
        List<String> todosOsTipos = new ArrayList<>();

        for (Projeto projeto : projetos) {
            todosOsTipos.addAll(projeto.getPerfis().stream()
                    .map(Perfil::getNome)
                    .collect(Collectors.toList()));
        }

        Collections.shuffle(todosOsTipos, random);
        int quantidadeDeTipos = 1 + random.nextInt(2);

        for (int i = 0; i < quantidadeDeTipos && i < todosOsTipos.size(); i++) {
            tiposCombinados.add(todosOsTipos.get(i));
        }

        return new ArrayList<>(tiposCombinados);
    }

}
