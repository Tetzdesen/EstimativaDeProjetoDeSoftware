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

    public Optional<Projeto> criarProjetoAleatorio() {
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
        //String dataCriacao = gerarDataAleatoria();
        String status = random.nextBoolean() ? "Estimado" : "Em andamento";
        boolean compartilhado = random.nextBoolean();
        String compartilhadoPor = compartilhado ? projetoBase.getCriador() : null;
        Perfil perfil = new Perfil("Android");
        perfis.add(perfil);

        // Criando campos para estimativa
        Map<String, Integer> tamanhosApp = perfil.getTamanhosApp();
        List<Campo> campos = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : tamanhosApp.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
        }

        // Criando campos para niveisUD
        Map<String, Double> niveisUI = perfil.getNiveisUI();
        for (Map.Entry<String, Double> entry : niveisUI.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
        }

        // Criando campos para funcionalidades
        Map<String, Integer> funcionalidades = perfil.getFuncionalidades();
        for (Map.Entry<String, Integer> entry : funcionalidades.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
        }

        // Criando campos para taxas diarias
        Map<String, Double> taxasDiarias = perfil.getTaxasDiarias();

        for (Map.Entry<String, Double> entry : taxasDiarias.entrySet()) {
            campos.add(new Campo(UUID.randomUUID(), "", entry.getKey(), entry.getValue()));
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

    private String gerarDataAleatoria() {
        Random random = new Random();
        int dia = random.nextInt(28) + 1;
        int mes = random.nextInt(12) + 1;
        int ano = 2023 + random.nextInt(3);
        return String.format("%02d/%02d/%d", dia, mes, ano);
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

    /*

    private Map<String, Integer> combinarFuncionalidades(List<Projeto> projetos, Random random) {
        Map<String, Integer> funcionalidadesCombinadas = new HashMap<>();
        int numProjetosParaCombinar = 1 + random.nextInt(projetos.size());

        for (int i = 0; i < numProjetosParaCombinar; i++) {
            Projeto projeto = projetos.get(random.nextInt(projetos.size()));
            List<Campo> funcionalidades = projeto.getCampos();

            int numFuncionalidades = 1 + random.nextInt(funcionalidades.size());
            List<String> chaves = new ArrayList<>(funcionalidades.);
            Collections.shuffle(chaves, random);

            for (int j = 0; j < numFuncionalidades; j++) {
                String chave = chaves.get(j);
                funcionalidadesCombinadas.putIfAbsent(chave, funcionalidades.get(chave));
            }
        }

        return funcionalidadesCombinadas;
    }*/
}
