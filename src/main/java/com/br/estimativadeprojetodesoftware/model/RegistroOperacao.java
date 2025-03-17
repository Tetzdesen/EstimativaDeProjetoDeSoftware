package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RegistroOperacao {
    private String operacao;
    private final LocalDateTime dataHora;
    private String usuario;
    private String mensagemFalha;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public RegistroOperacao(String operacao, String usuario) {
        this.operacao = operacao;
        this.dataHora = LocalDateTime.now();
        this.usuario = usuario;
        this.mensagemFalha = null;
    }

    public RegistroOperacao(String operacao, String usuario, String mensagemFalha) {
        this.operacao = operacao;
        this.dataHora = LocalDateTime.now();
        this.usuario = usuario;
        this.mensagemFalha = mensagemFalha;
    }

    public String getDataHoraFormatada() {
        return dataHora.format(FORMATTER);
    }

    public String formatarLog() {
        if (mensagemFalha == null) {
            return String.format("\"%s\": \"%s\", (%s)", operacao, getDataHoraFormatada(), usuario);
        } else {
            return String.format("Ocorreu a falha \"%s\" ao realizar a operação \"%s\", (%s, %s).",
                    mensagemFalha, operacao, getDataHoraFormatada(), usuario);
        }
    }

    // Getters e Setters
    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensagemFalha() {
        return mensagemFalha;
    }

    public void setMensagemFalha(String mensagemFalha) {
        this.mensagemFalha = mensagemFalha;
    }
}
