package com.br.estimativadeprojetodesoftware.principal;

import com.br.estimativadeprojetodesoftware.presenter.usuario.LoginPresenter;
import com.br.estimativadeprojetodesoftware.presenter.PrincipalPresenter;
import com.br.estimativadeprojetodesoftware.repository.PerfilRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.singleton.UsuarioLogadoSingleton;
import javax.swing.SwingUtilities;

/**
 *
 * @author tetzner
 */
public class Principal {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ProjetoRepositoryMock projetoRepository = new ProjetoRepositoryMock();
            UsuarioRepositoryMock usuarioRepository = new UsuarioRepositoryMock();
            PerfilRepositoryMock perfilRepository = new PerfilRepositoryMock();


            new LoginPresenter(projetoRepository, usuarioRepository);

       
            if (UsuarioLogadoSingleton.getInstancia().getUsuario() != null) {
   
                new PrincipalPresenter(projetoRepository, usuarioRepository, perfilRepository);
            }


        });
    }
}
