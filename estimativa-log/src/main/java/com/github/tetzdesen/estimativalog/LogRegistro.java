package com.github.tetzdesen.estimativalog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Registro de uma operação do sistema. Gera os textos exatos exigidos pela spec
 * log-sistema para sucesso e para falha.
 */
public final class LogRegistro {

    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String operacao;
    private final String nomeProjeto;
    private final String data;
    private final String hora;
    private final String usuario;
    private final String mensagemFalha;

    public LogRegistro(String operacao, String nomeProjeto, String usuario, String mensagemFalha, LocalDateTime dataHora) {
        this.operacao = obrigatorio(operacao, "operação");
        this.nomeProjeto = obrigatorio(nomeProjeto, "nome do projeto");
        this.usuario = obrigatorio(usuario, "usuário");
        Objects.requireNonNull(dataHora, "Data e hora não podem ser nulas");
        this.data = dataHora.format(FORMATO_DATA);
        this.hora = dataHora.format(FORMATO_HORA);
        this.mensagemFalha = (mensagemFalha == null || mensagemFalha.isBlank()) ? null : mensagemFalha;
    }

    public static LogRegistro sucesso(String operacao, String nomeProjeto, String usuario) {
        return new LogRegistro(operacao, nomeProjeto, usuario, null, LocalDateTime.now());
    }

    public static LogRegistro falha(String operacao, String nomeProjeto, String usuario, String mensagemFalha) {
        String mensagem = (mensagemFalha == null || mensagemFalha.isBlank()) ? "desconhecida" : mensagemFalha;
        return new LogRegistro(operacao, nomeProjeto, usuario, mensagem, LocalDateTime.now());
    }

    private static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório do log vazio: " + campo);
        }
        return valor;
    }

    public boolean isFalha() {
        return mensagemFalha != null;
    }

    /**
     * Texto do registro conforme a spec:
     * sucesso: "OPERACAO: NOME_PROJETO, (DATA, HORA, e USUARIO)"
     * falha: "Ocorreu a falha MENSAGEM ao realizar a OPERACAO do projeto NOME_PROJETO, (DATA, HORA, e USUARIO)."
     */
    public String getMensagem() {
        String sufixo = "(" + data + ", " + hora + ", e " + usuario + ")";
        if (isFalha()) {
            return "Ocorreu a falha " + mensagemFalha + " ao realizar a " + operacao
                    + " do projeto " + nomeProjeto + ", " + sufixo + ".";
        }
        return operacao + ": " + nomeProjeto + ", " + sufixo;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMensagemFalha() {
        return mensagemFalha;
    }

    @Override
    public String toString() {
        return getMensagem();
    }
}
