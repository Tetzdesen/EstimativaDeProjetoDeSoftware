package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.ICompartilhamentoRepository;
import com.br.estimativadeprojetodesoftware.repository.IConfiguracaoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;

/**
 * Abstract Factory dos repositórios: uma fábrica concreta por banco, escolhida
 * pelo TIPO_BANCO do .env.
 */
public interface FabricaRepository {

    IUsuarioRepository criarUsuarioRepository();

    IProjetoRepository criarProjetoRepository();

    IPerfilRepository criarPerfilRepository();

    ICompartilhamentoRepository criarCompartilhamentoRepository();

    IConfiguracaoRepository criarConfiguracaoRepository();
}
