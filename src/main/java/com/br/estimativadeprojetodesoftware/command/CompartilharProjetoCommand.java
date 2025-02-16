/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.estimativadeprojetodesoftware.command;

import com.br.estimativadeprojetodesoftware.model.Projeto;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.ProjetoRepositoryMock;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
//import javax.swing.JDesktopPane;

/**
 *
 * @author flamo
 */
public class CompartilharProjetoCommand implements ProjetoCommand {
//    private final JDesktopPane desktop;

    private final UsuarioRepositoryMock usuarioRepository;
    private final ProjetoRepositoryMock projetoRepository;
    private Usuario usuarioRemetente;
    private Usuario usuarioDestinatario;
    private Projeto projeto;

    public CompartilharProjetoCommand(/*JDesktopPane desktop,*/UsuarioRepositoryMock usuarioRepository, ProjetoRepositoryMock projetoRepository, Usuario usuarioRemetente, Usuario usuarioDestinatario, Projeto projeto) {
//        this.desktop = desktop;
        this.usuarioRepository = usuarioRepository;
        this.projetoRepository = projetoRepository;
        this.usuarioRemetente = usuarioRemetente;
        this.usuarioDestinatario = usuarioDestinatario;
        this.projeto = projeto;
    }

    @Override
    public void execute() {
        
        try {
            //add projeto na lista de compartilhados do destinatario
            usuarioDestinatario.getProjetosCompartilhados().add(projeto);
        } catch (Exception ignored) {
        }

    }
}
