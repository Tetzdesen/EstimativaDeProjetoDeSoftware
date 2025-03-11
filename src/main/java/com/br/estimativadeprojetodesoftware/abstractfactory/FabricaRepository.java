package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.ICampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;

/**
 *
 * @author tetzner
 */
public interface FabricaRepository {
    IUsuarioRepository criarUsuarioRepository();
    IProjetoRepository criarProjetoRepository();
    IPerfilRepository criarPerfilRepository();
    ICampoRepository criarCampoRepository();
}
