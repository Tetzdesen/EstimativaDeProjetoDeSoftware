package com.br.estimativadeprojetodesoftware.presenter.usuario;

import com.br.estimativadeprojetodesoftware.command.MostrarMensagemProjetoCommand;
import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.UsuarioRepositoryMock;
import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.service.ValidadorSenhaService;
import com.br.estimativadeprojetodesoftware.view.usuario.CadastroView;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author tetzner
 */
public class CadastroPresenter {

    private CadastroView view;
    private UsuarioRepositoryMock repositoryUsuario;
    private ValidadorSenhaService validadorDeSenha;

    public CadastroPresenter(UsuarioRepositoryMock repositoryUsuario) {
        this.view = new CadastroView();
        this.repositoryUsuario = repositoryUsuario;
        this.validadorDeSenha = new ValidadorSenhaService();
        configuraView();
    }

    private void configuraView() {
       // view.getLblFoto().setBounds(20, 30, 100, 100);
      //  view.getLblFoto().setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
      //  view.getLblFoto().setHorizontalAlignment(JLabel.CENTER);
      //  view.getLblFoto().setVerticalAlignment(JLabel.CENTER);
      //  view.getLblFoto().setOpaque(true);
     //   view.getLblFoto().setBackground(Color.DARK_GRAY);
      //  view.getLblFoto().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        view.setResizable(false);
        view.setLocationRelativeTo(null);
        configuraActionsListerns();
        view.setVisible(true);
    }

    private void configuraActionsListerns() {

        // Adiciona evento de clique para abrir o seletor de arquivos
        view.getLblFoto().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarFoto();
            }
        });

        view.getBtnSalvar().addActionListener(e -> {
            try {
                efetuarCadastro();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnCancelar().addActionListener(e -> {
            try {
                view.dispose();
            } catch (Exception ex) {
                exibirMensagem(ex.getMessage());
            }
        });

        view.getBtnExibirSenha().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho-exibido"));
                view.getTxtSenha().setEchoChar('\0');
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                view.getBtnExibirSenha().setIcon(IconService.getIcon("olho"));
                view.getTxtSenha().setEchoChar('*');
            }
        });

        view.getBtnExibirConfirmarSenha().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.getBtnExibirConfirmarSenha().setIcon(IconService.getIcon("olho-exibido"));
                view.getTxtConfirmarSenha().setEchoChar('\0');
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                view.getBtnExibirConfirmarSenha().setIcon(IconService.getIcon("olho"));
                view.getTxtConfirmarSenha().setEchoChar('*');
            }
        });
    }

    private void efetuarCadastro() throws Exception {

        String email = view.getTxtEmail().getText();

        Usuario usuario = repositoryUsuario.getUsuarioPorEmail(email);

        if (usuario == null) {
            String nome = view.getTxtNomeUsuario().getText();
            String senha = new String(view.getTxtSenha().getPassword());
            String senhaConfirmada = new String(view.getTxtConfirmarSenha().getPassword());

            if (camposInvalidos(email, nome, senha, senhaConfirmada)) {
                throw new IllegalArgumentException("Os campos não podem estar vazios");
            }

            if (senha.equals(senhaConfirmada)) {
                try {
                    if (validadorDeSenha.validarSenha(senha)) {
                        repositoryUsuario.adicionarUsuario(nome, email, senha);
                        exibirMensagem("Cadastro realizado com sucesso!");
                        view.dispose();
                    }
                } catch (Exception ex) {
                    throw new Exception("Erro na validação da senha:\n" + ex.getMessage());
                }

            } else {
                throw new IllegalArgumentException("Senhas não conferem");
            }
        } else {
            throw new IllegalArgumentException("Usuário já cadastrado no sistema, por favor cadastrar outro usuário");
        }
    }
    
    private void selecionarFoto() {
        JFileChooser fileChooser = view.getJfcSelecionarFoto();
        fileChooser.setDialogTitle("Selecionar Foto");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int retorno = fileChooser.showOpenDialog(view);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            exibirImagem(arquivoSelecionado);
        }
    }
    
     private void exibirImagem(File file) {
        try {
            BufferedImage imagem = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(imagem.getScaledInstance(view.getLblFoto().getWidth(), view.getLblFoto().getHeight(), Image.SCALE_SMOOTH));
            view.getLblFoto().setIcon(icon);
            view.getLblFoto().setText(""); // Remove qualquer texto
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar a imagem.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public CadastroView getView() {
        return view;
    }

    private boolean camposInvalidos(String email, String nome, String senha, String senhaConfirmada) {
        return email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nome == null || nome.trim().isEmpty() || senhaConfirmada == null || senhaConfirmada.isEmpty();
    }

    public void exibirMensagem(String mensagem) {
        new MostrarMensagemProjetoCommand(mensagem).execute();
    }
}
