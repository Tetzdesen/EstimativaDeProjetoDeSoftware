package com.br.estimativadeprojetodesoftware.service;

import com.br.estimativadeprojetodesoftware.abstractfactory.SeletorFabricaRepository;
import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Entidade;
import com.br.estimativadeprojetodesoftware.observer.EventoDominio.Tipo;
import com.br.estimativadeprojetodesoftware.observer.Subject;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * CRUD de perfis (BR01, BR04). Perfis padrão são visíveis e alteráveis por
 * todos, mas protegidos contra exclusão; perfis personalizados só pelo dono.
 */
public class PerfilService extends ServicoObservavel {

    private final IPerfilRepository repository;

    public PerfilService(IPerfilRepository repository, Subject publicador) {
        super(publicador);
        this.repository = Objects.requireNonNull(repository);
    }

    public PerfilService() {
        this(SeletorFabricaRepository.obterInstancia().criarPerfilRepository(), null);
    }

    public List<Perfil> listarVisiveis(Usuario usuario) {
        return repository.buscarVisiveisPara(usuario.getId());
    }

    public Optional<Perfil> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    /** Novo perfil do usuário, partindo de uma cópia de outro perfil (ou vazio, se base for nula). */
    public Perfil criar(String nome, Perfil base, Usuario usuario) {
        Perfil perfil = base == null ? new Perfil(nome, usuario.getId()) : base.clonar(nome, usuario.getId());
        repository.salvar(perfil);
        publicar(Tipo.CRIACAO, Entidade.PERFIL, perfil.getId());
        return perfil;
    }

    /** Grava um perfil novo já montado (ex.: cópia editada antes de salvar). */
    public void incluir(Perfil perfil, Usuario usuario) {
        if (perfil.isPadrao() || !usuario.getId().equals(perfil.getDonoId())) {
            throw new IllegalStateException("O novo perfil precisa pertencer ao usuário logado.");
        }
        repository.salvar(perfil);
        publicar(Tipo.CRIACAO, Entidade.PERFIL, perfil.getId());
    }

    public void salvar(Perfil perfil, Usuario usuario) {
        verificarPodeAlterar(perfil, usuario);
        repository.salvar(perfil);
        publicar(Tipo.ALTERACAO, Entidade.PERFIL, perfil.getId());
    }

    public void excluir(UUID id, Usuario usuario) {
        Perfil perfil = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado."));
        if (perfil.isPadrao()) {
            throw new IllegalStateException("O perfil padrão não pode ser excluído: ele é a base fornecida pelo sistema.");
        }
        verificarPodeAlterar(perfil, usuario);
        repository.remover(id);
        publicar(Tipo.EXCLUSAO, Entidade.PERFIL, id);
    }

    public boolean podeAlterar(Perfil perfil, Usuario usuario) {
        return usuario != null && (perfil.isPadrao() || usuario.getId().equals(perfil.getDonoId()));
    }

    public boolean podeExcluir(Perfil perfil, Usuario usuario) {
        return !perfil.isPadrao() && podeAlterar(perfil, usuario);
    }

    private void verificarPodeAlterar(Perfil perfil, Usuario usuario) {
        if (!podeAlterar(perfil, usuario)) {
            throw new IllegalStateException("Somente o dono pode alterar este perfil.");
        }
    }

    public long contarDoUsuario(Usuario usuario) {
        return listarVisiveis(usuario).stream().filter(p -> usuario.getId().equals(p.getDonoId())).count();
    }
}
