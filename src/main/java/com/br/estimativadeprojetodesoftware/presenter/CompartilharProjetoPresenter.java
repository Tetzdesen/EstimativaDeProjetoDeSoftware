package com.br.estimativadeprojetodesoftware.presenter;

import com.br.estimativadeprojetodesoftware.model.Perfil;
import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.EstimaProjetoService;
import com.br.estimativadeprojetodesoftware.view.CompartilharProjetoView;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class CompartilharProjetoPresenter /*implements Observer*/ {

    private final CompartilharProjetoView view;
    private final EstimaProjetoService estimaService;
    private final UsuarioRepositoryMock repository;

    public CompartilharProjetoPresenter(CompartilharProjetoView view, UsuarioRepositoryMock repository) {
        this.view = view;
        this.repository = repository;
        this.estimaService = new EstimaProjetoService();
//        this.repository.addObserver(this);
        carregarListaUsuarios();
    }

    private void carregarListaUsuarios() {
        List<Usuario> usuarioList = repository.getUsuarios();

        for (Usuario usuario : usuarioList) {
            if (usuario != null) {
                carregarCabecalho(usuario);
                carregarDetalhes(usuario);
            }
        }
    }

    private void carregarDetalhes(Usuario usuario) {
        Object[][] dadosTabela = {
            {usuario.getNome(), usuario.getEmail()}
        };

        view.atualizarTabela(dadosTabela);
    }

    private void carregarCabecalho(Usuario usuario) {
        String tiposConcatenados = usuario.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.joining(", "));

        view.atualizarCabecalho(
                usuario.getNome(),
                usuario.getEmail()
        );
    }

//    @Override
//    public void update() {
//        carregarDetalhes();
//    }
}
