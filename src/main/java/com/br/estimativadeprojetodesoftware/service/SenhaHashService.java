package com.br.estimativadeprojetodesoftware.service;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Responsável por transformar a senha em texto puro em um hash seguro antes de
 * persistir no banco.
 *
 * O formato armazenado é: {@code pbkdf2_sha256$<iteracoes>$<salt>$<hash>}, onde
 * salt e hash estão em Base64. O salt fica junto do hash porque ele precisa ser
 * recuperado para recalcular o hash de uma senha digitada.
 *
 * @author tetzner
 */
public final class SenhaHashService {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIXO = "pbkdf2_sha256";
    private static final String SEPARADOR = "\\$";
    private static final int ITERACOES = 120_000;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final int TAMANHO_SALT_BYTES = 16;

    private static final SecureRandom RANDOM = new SecureRandom();

    private SenhaHashService() {
        // Construtor privado para evitar instância
    }

    /**
     * Gera o hash de uma senha nova, com um salt aleatório.
     */
    public static String gerarHash(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Erro: Senha não pode ser vazia");
        }

        byte[] salt = new byte[TAMANHO_SALT_BYTES];
        RANDOM.nextBytes(salt);

        return montarHash(senha, salt, ITERACOES);
    }

    /**
     * Recalcula o hash de uma senha digitada usando o salt e as iterações do
     * hash já armazenado. O resultado é igual ao armazenado quando a senha
     * digitada está correta.
     */
    public static String recalcularHash(String senha, String hashArmazenado) {
        if (senha == null || !estaHasheada(hashArmazenado)) {
            throw new IllegalArgumentException("Erro: Hash armazenado em formato inválido");
        }

        String[] partes = hashArmazenado.split(SEPARADOR);
        int iteracoes = Integer.parseInt(partes[1]);
        byte[] salt = Base64.getDecoder().decode(partes[2]);

        return montarHash(senha, salt, iteracoes);
    }

    /**
     * Confere se a senha digitada corresponde ao hash armazenado.
     */
    public static boolean verificar(String senha, String hashArmazenado) {
        if (senha == null || !estaHasheada(hashArmazenado)) {
            return false;
        }
        return constantTimeEquals(recalcularHash(senha, hashArmazenado), hashArmazenado);
    }

    /**
     * Indica se o valor já está no formato de hash deste serviço.
     */
    public static boolean estaHasheada(String valor) {
        if (valor == null) {
            return false;
        }
        String[] partes = valor.split(SEPARADOR);
        return partes.length == 4 && PREFIXO.equals(partes[0]);
    }

    private static String montarHash(String senha, byte[] salt, int iteracoes) {
        try {
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, TAMANHO_HASH_BITS);
            byte[] hash = SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();

            Base64.Encoder encoder = Base64.getEncoder();
            return PREFIXO + "$" + iteracoes + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o hash da senha: " + e.getMessage(), e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int diferenca = 0;
        for (int i = 0; i < a.length(); i++) {
            diferenca |= a.charAt(i) ^ b.charAt(i);
        }
        return diferenca == 0;
    }
}
