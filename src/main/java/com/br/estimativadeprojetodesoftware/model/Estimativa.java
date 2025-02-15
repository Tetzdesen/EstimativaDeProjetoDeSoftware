package com.br.estimativadeprojetodesoftware.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Estimativa {

    private UUID id;
    private LocalDateTime created_at;
    private Map<String, Integer> campos;
    
    public Estimativa() {
        this.id = UUID.randomUUID();
        this.created_at = LocalDateTime.now();
        this.campos = new HashMap<>();
    }

    public Estimativa(UUID id, LocalDateTime created_at, Map<String, Integer> campos) {
        this.id = id;
        this.created_at = created_at;
        this.campos = campos;
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
            throw new IllegalArgumentException("Erro: Nome da funcionalidade não pode ser vazia ou nula.");
        }
        if (dias < 0) {
            throw new IllegalArgumentException("Erro: Quantidade de dias não pode ser negativo. Chave: " + nomeFuncionalidade + " Valor: " + dias);
        }
        campos.put(nomeFuncionalidade, dias);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estimativa other = (Estimativa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.created_at, other.created_at)) {
            return false;
        }
        return Objects.equals(this.campos, other.campos);
    }
    
    @Override
    public String toString() {
        return "Estimativa{" + "id=" + id + ", created_at=" + created_at + ", campos=" + campos + '}';
    }
}
