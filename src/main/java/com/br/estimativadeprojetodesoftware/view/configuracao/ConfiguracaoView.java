package com.br.estimativadeprojetodesoftware.view.configuracao;

import com.br.estimativadeprojetodesoftware.service.IconService;
import com.br.estimativadeprojetodesoftware.view.componentes.Estilos;
import com.br.estimativadeprojetodesoftware.view.componentes.JanelaInterna;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/** Tela de Configuração: aparência (tema) e formato do log. Passive View. */
public class ConfiguracaoView extends JanelaInterna {

    private final JRadioButton rbClaro = new JRadioButton("Claro");
    private final JRadioButton rbEscuro = new JRadioButton("Escuro");
    private final JRadioButton rbCsv = new JRadioButton("CSV (campos separados por ponto e vírgula)");
    private final JRadioButton rbJson = new JRadioButton("JSON");
    private final JLabel lblArquivo = Estilos.aplicar(new JLabel(" "), Estilos.ROTULO_PEQUENO);
    private final JButton btnSalvar;
    private final JButton btnFechar;

    public ConfiguracaoView() {
        super("Configuração", "Escolha o tema e o formato do log e clique em Salvar. As escolhas continuam valendo depois de reiniciar.", "configuracao");
        btnSalvar = getBarra().adicionarPrincipal("Salvar", "salvar");
        getBarra().empurrarParaDireita();
        btnFechar = getBarra().adicionar("Fechar", "fechar");
        setBotaoSalvar(btnSalvar);
        ButtonGroup tema = new ButtonGroup();
        tema.add(rbClaro);
        tema.add(rbEscuro);
        ButtonGroup log = new ButtonGroup();
        log.add(rbCsv);
        log.add(rbJson);

        JPanel aparencia = secao("Aparência", "Tema da interface");
        JPanel temas = new JPanel(new GridLayout(1, 2, 12, 0));
        temas.setOpaque(false);
        temas.add(opcaoTema(rbClaro, "tema-claro"));
        temas.add(opcaoTema(rbEscuro, "tema-escuro"));
        aparencia.add(temas);

        JPanel secaoLog = secao("Log de operações", "Formato do arquivo que registra criação, alteração, exclusão, compartilhamento e exportação");
        secaoLog.add(rbCsv);
        secaoLog.add(rbJson);
        secaoLog.add(lblArquivo);

        JPanel coluna = new JPanel(new GridLayout(0, 1, 0, 12));
        coluna.setOpaque(false);
        coluna.add(aparencia);
        coluna.add(secaoLog);
        for (JPanel secao : new JPanel[]{aparencia, secaoLog}) {
            for (java.awt.Component c : secao.getComponents()) {
                ((javax.swing.JComponent) c).setAlignmentX(LEFT_ALIGNMENT);
            }
        }
        getConteudo().add(coluna, BorderLayout.NORTH);
        setSize(620, 470);
    }

    private static JPanel secao(String titulo, String descricao) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        Estilos.aplicar(p, Estilos.CARTAO);
        p.add(Estilos.aplicar(new JLabel(titulo), Estilos.SECAO));
        p.add(Estilos.aplicar(new JLabel(descricao), Estilos.ROTULO_PEQUENO));
        return p;
    }

    private static JPanel opcaoTema(JRadioButton rb, String icone) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(IconService.getIcon(icone, 24));
        l.setLabelFor(rb);
        p.add(l, BorderLayout.WEST);
        p.add(rb, BorderLayout.CENTER);
        return p;
    }

    public JRadioButton getRbClaro() {
        return rbClaro;
    }

    public JRadioButton getRbEscuro() {
        return rbEscuro;
    }

    public JRadioButton getRbCsv() {
        return rbCsv;
    }

    public JRadioButton getRbJson() {
        return rbJson;
    }

    public JLabel getLblArquivo() {
        return lblArquivo;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}
