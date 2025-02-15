package com.br.estimativadeprojetodesoftware.service;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconService {

    private static final Map<String, ImageIcon> icons = new HashMap<>();

    static {
        icons.put("principal", loadIcon("principal-icon.png"));
        icons.put("usuario", loadIcon("usuario-icon.png"));
        icons.put("perfil", loadIcon("perfil-icon.png"));
        icons.put("projeto", loadIcon("projeto-icon-tree.png"));
        icons.put("action", loadIcon("action-icon.png"));
        icons.put("logout", loadIcon("logout-icon.png"));
        icons.put("olho", loadIcon("olho-icon.png"));
        icons.put("olho-exibido", loadIcon("olho-exibido-icon.png"));
        icons.put("salvar", loadIcon("salvar-icon.png"));
        icons.put("editar", loadIcon("editar-icon.png"));
        icons.put("excluir", loadIcon("excluir-icon.png"));
    }

    private static ImageIcon loadIcon(String path) {
        return new ImageIcon(
                Objects.requireNonNull(IconService.class.getClassLoader().getResource(path))
        );
    }

    public static ImageIcon getIcon(String key) {
        return icons.get(key);
    }
}
