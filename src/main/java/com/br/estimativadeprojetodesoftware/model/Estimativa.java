package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Estimativa {

    private final UUID id;
    private final LocalDateTime created_at;
    private final Map<String, Integer> campos;

    public Estimativa() {
        this.id = UUID.randomUUID();
        this.created_at = LocalDateTime.now();
        this.campos = new HashMap<>();
    }

    public Estimativa(UUID id, LocalDateTime created_at, Map<String, Integer> campos) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.created_at = Objects.requireNonNull(created_at, "Data de criação não pode ser nula");
        this.campos = campos != null ? new HashMap<>(campos) : new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public Map<String, Integer> getCampos() {
        return Collections.unmodifiableMap(campos);
    }

    public void adicionarCampo(String nomeFuncionalidade, int dias) {
        if (nomeFuncionalidade == null || nomeFuncionalidade.isEmpty()) {
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazio ou nulo.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: Quantidade de dias não pode ser negativa. Chave: " + nomeFuncionalidade + " Valor: " + dias);
        }
        campos.put(nomeFuncionalidade, dias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Estimativa other = (Estimativa) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Estimativa{"
                + "id=" + id
                + ", created_at=" + created_at
                + ", campos=" + campos
                + '}';
    }
}
