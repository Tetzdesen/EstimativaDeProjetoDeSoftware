package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.model.Estimativa;
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
        Map<String, Integer> funcionalidades = combinarFuncionalidades(projetosExistentes, random);
        Estimativa estimativa = new Estimativa(UUID.randomUUID(), LocalDateTime.now(), funcionalidades);
        Map<String, Double> taxasDiarias = new HashMap<>();
        taxasDiarias.put("Desenvolvimento", 450.00);
        List<Usuario> usuarios = new ArrayList();
        usuarios.add(UsuarioLogadoSingleton.getInstancia().getUsuario());
        return Optional.of(new Projeto(UUID.randomUUID(), nome, criador, "Android", LocalDateTime.now(), null, null, status, compartilhado, compartilhadoPor, taxasDiarias, perfis, usuarios, estimativa));
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

    private Map<String, Integer> combinarFuncionalidades(List<Projeto> projetos, Random random) {
        Map<String, Integer> funcionalidadesCombinadas = new HashMap<>();
        int numProjetosParaCombinar = 1 + random.nextInt(projetos.size());

        for (int i = 0; i < numProjetosParaCombinar; i++) {
            Projeto projeto = projetos.get(random.nextInt(projetos.size()));
            Map<String, Integer> funcionalidades = projeto.getEstimativa().getCampos();

            int numFuncionalidades = 1 + random.nextInt(funcionalidades.size());
            List<String> chaves = new ArrayList<>(funcionalidades.keySet());
            Collections.shuffle(chaves, random);

            for (int j = 0; j < numFuncionalidades; j++) {
                String chave = chaves.get(j);
                funcionalidadesCombinadas.putIfAbsent(chave, funcionalidades.get(chave));
            }
        }

        return funcionalidadesCombinadas;
    }
}
