package com.br.estimativadeprojetodesoftware.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Projeto de estimativa. Contém cópias independentes das funcionalidades dos
 * perfis de origem, as plataformas marcadas, taxas, custos adicionais,
 * impostos e lucro.
 */
public class Projeto {

    public static final BigDecimal IMPOSTOS_PADRAO = new BigDecimal("0.42");
    public static final BigDecimal LUCRO_PADRAO = new BigDecimal("0.30");

    private final UUID id;
    private String nome;
    private final UUID donoId;
    private String donoNome;
    private final Set<Plataforma> plataformas;
    private final List<ItemProjeto> itens;
    private TaxaDiaria taxas;
    private final List<CustoAdicional> custosAdicionais;
    private BigDecimal percentualImpostos;
    private BigDecimal percentualLucro;
    private boolean calculado;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Projeto(UUID id, String nome, UUID donoId, TaxaDiaria taxas, BigDecimal percentualImpostos,
            BigDecimal percentualLucro, boolean calculado, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = Objects.requireNonNull(id);
        setNome(nome);
        this.donoId = Objects.requireNonNull(donoId, "O projeto precisa de um dono");
        this.plataformas = EnumSet.noneOf(Plataforma.class);
        this.itens = new ArrayList<>();
        this.taxas = taxas == null ? new TaxaDiaria() : taxas;
        this.custosAdicionais = new ArrayList<>();
        setPercentualImpostos(percentualImpostos == null ? IMPOSTOS_PADRAO : percentualImpostos);
        setPercentualLucro(percentualLucro == null ? LUCRO_PADRAO : percentualLucro);
        this.calculado = calculado;
        this.criadoEm = criadoEm == null ? LocalDateTime.now() : criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    /**
     * Cria um projeto a partir de um ou mais perfis usando o Prototype
     * ({@link Perfil#copiarParaProjeto()}). Funcionalidades repetidas
     * (mesma categoria e nome) entram uma única vez; as taxas vêm do primeiro perfil.
     */
    public static Projeto aPartirDePerfis(String nome, UUID donoId, List<Perfil> perfis) {
        if (perfis == null || perfis.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um perfil para criar o projeto");
        }
        Projeto projeto = new Projeto(UUID.randomUUID(), nome, donoId, perfis.get(0).getTaxas().copiar(),
                IMPOSTOS_PADRAO, LUCRO_PADRAO, false, LocalDateTime.now(), null);
        int ordem = 0;
        for (Perfil perfil : perfis) {
            for (ItemProjeto item : perfil.copiarParaProjeto()) {
                boolean repetido = projeto.itens.stream()
                        .anyMatch(i -> i.getFuncionalidade().mesmaChave(item.getFuncionalidade()));
                if (!repetido) {
                    item.getFuncionalidade().setOrdem(++ordem);
                    projeto.itens.add(item);
                }
            }
        }
        projeto.plataformas.addAll(EnumSet.allOf(Plataforma.class));
        CustoAdicional.padroes().forEach(projeto.custosAdicionais::add);
        return projeto;
    }

    // ---------- plataformas ----------

    public Set<Plataforma> getPlataformas() {
        return Collections.unmodifiableSet(EnumSet.copyOf(plataformas));
    }

    public void setPlataformaMarcada(Plataforma plataforma, boolean marcada) {
        if (marcada) {
            plataformas.add(plataforma);
        } else {
            plataformas.remove(plataforma);
        }
    }

    public boolean isPlataformaMarcada(Plataforma plataforma) {
        return plataformas.contains(plataforma);
    }

    // ---------- itens ----------

    public List<ItemProjeto> getItens() {
        List<ItemProjeto> ordenados = new ArrayList<>(itens);
        ordenados.sort(Comparator.comparingInt(i -> i.getFuncionalidade().getOrdem()));
        return Collections.unmodifiableList(ordenados);
    }

    public void adicionarItem(ItemProjeto item) {
        itens.add(Objects.requireNonNull(item));
    }

    public Optional<ItemProjeto> buscarItem(UUID idFuncionalidade) {
        return itens.stream().filter(i -> i.getFuncionalidade().getId().equals(idFuncionalidade)).findFirst();
    }

    public Optional<ItemProjeto> buscarItem(String categoria, String nome) {
        return itens.stream()
                .filter(i -> i.getFuncionalidade().getCategoria().equalsIgnoreCase(categoria)
                && i.getFuncionalidade().getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Marca ou desmarca um item. Em categorias de escolha única, marcar um
     * item desmarca os demais da categoria. Itens não aplicáveis em todas as
     * plataformas não podem ser selecionados.
     */
    public void selecionar(UUID idFuncionalidade, boolean selecionado) {
        ItemProjeto item = buscarItem(idFuncionalidade)
                .orElseThrow(() -> new IllegalArgumentException("Funcionalidade não encontrada no projeto"));
        Funcionalidade f = item.getFuncionalidade();
        if (selecionado && f.getTipoSelecao() != TipoSelecao.LIVRE) {
            boolean aplicavelEmAlguma = EnumSet.allOf(Plataforma.class).stream().anyMatch(f::isAplicavel);
            if (!aplicavelEmAlguma) {
                throw new IllegalArgumentException("\"" + f.getNome() + "\" não é aplicável e não pode ser selecionada");
            }
        }
        if (selecionado && f.getTipoSelecao() == TipoSelecao.UNICA) {
            for (ItemProjeto outro : itens) {
                if (outro != item && outro.getFuncionalidade().getCategoria().equalsIgnoreCase(f.getCategoria())) {
                    outro.setSelecionado(false);
                }
            }
        }
        item.setSelecionado(selecionado);
    }

    // ---------- custos ----------

    public List<CustoAdicional> getCustosAdicionais() {
        return Collections.unmodifiableList(custosAdicionais);
    }

    public void adicionarCustoAdicional(CustoAdicional custo) {
        custosAdicionais.add(Objects.requireNonNull(custo));
    }

    public void removerCustoAdicional(UUID idCusto) {
        custosAdicionais.removeIf(c -> c.getId().equals(idCusto));
    }

    public void substituirCustosAdicionais(List<CustoAdicional> novos) {
        custosAdicionais.clear();
        novos.forEach(this::adicionarCustoAdicional);
    }

    // ---------- demais atributos ----------

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do projeto não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public UUID getDonoId() {
        return donoId;
    }

    public boolean isDono(UUID usuarioId) {
        return donoId.equals(usuarioId);
    }

    public String getDonoNome() {
        return donoNome;
    }

    public void setDonoNome(String donoNome) {
        this.donoNome = donoNome;
    }

    public TaxaDiaria getTaxas() {
        return taxas;
    }

    public void setTaxas(TaxaDiaria taxas) {
        this.taxas = Objects.requireNonNull(taxas);
    }

    /** Percentual de impostos como fração (0,42 = 42%). */
    public BigDecimal getPercentualImpostos() {
        return percentualImpostos;
    }

    public final void setPercentualImpostos(BigDecimal percentual) {
        this.percentualImpostos = validarPercentual(percentual, "impostos");
    }

    /** Percentual de lucro como fração (0,30 = 30%). */
    public BigDecimal getPercentualLucro() {
        return percentualLucro;
    }

    public final void setPercentualLucro(BigDecimal percentual) {
        this.percentualLucro = validarPercentual(percentual, "lucro");
    }

    private static BigDecimal validarPercentual(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("O percentual de " + campo + " deve ser zero ou positivo");
        }
        return valor;
    }

    public boolean isCalculado() {
        return calculado;
    }

    public void setCalculado(boolean calculado) {
        this.calculado = calculado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    @Override
    public String toString() {
        return nome;
    }
}
