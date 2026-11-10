package com.br.estimativadeprojetodesoftware.adapter.senha;

import com.pss.senha.validacao.ValidadorSenha;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapter que envolve o validador de senhas do JitPack
 * (com.github.claytonfraga:validadorsenha). Só esta classe conhece a API externa.
 * Também adapta as mensagens para a tela: a biblioteca escreve sem acentos e
 * termina cada regra com ";".
 */
public class ValidadorSenhaAdapter implements IValidadorSenha {

    private static final Map<String, String> ACENTOS = Map.ofEntries(
            Map.entry("nao", "não"), Map.entry("minimo", "mínimo"), Map.entry("maximo", "máximo"),
            Map.entry("maiusculas", "maiúsculas"), Map.entry("minusculas", "minúsculas"),
            Map.entry("numeros", "números"), Map.entry("sequencia", "sequência"), Map.entry("simbolos", "símbolos"),
            Map.entry("digitos", "dígitos"), Map.entry("esta", "está"), Map.entry("ate", "até"), Map.entry("e nulo", "é nulo"));
    private static final Pattern PALAVRA = Pattern.compile("\\b(" + String.join("|", ACENTOS.keySet()) + ")\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final ValidadorSenha validador = new ValidadorSenha();

    @Override
    public List<String> validar(String senha) {
        List<String> mensagens = validador.validar(senha == null ? "" : senha);
        List<String> adaptadas = new ArrayList<>();
        if (mensagens != null) {
            for (String m : mensagens) {
                adaptadas.add(adaptar(m));
            }
        }
        return adaptadas;
    }

    /** "Senha nao deve possuir uma sequencia de letras;" → "Senha não deve possuir uma sequência de letras." */
    static String adaptar(String mensagem) {
        if (mensagem == null) {
            return "";
        }
        String texto = mensagem.strip();
        while (texto.endsWith(";")) {
            texto = texto.substring(0, texto.length() - 1).strip();
        }
        Matcher m = PALAVRA.matcher(texto);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String original = m.group(1);
            String trocada = ACENTOS.get(original.toLowerCase());
            if (Character.isUpperCase(original.charAt(0))) {
                trocada = Character.toUpperCase(trocada.charAt(0)) + trocada.substring(1);
            }
            m.appendReplacement(sb, Matcher.quoteReplacement(trocada));
        }
        m.appendTail(sb);
        texto = sb.toString();
        return texto.isEmpty() || texto.endsWith(".") ? texto : texto + ".";
    }
}
