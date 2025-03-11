package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.IEstimativaHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoHasPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioHasProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;

/**
 *
 * @author tetzner
 */
public interface FabricaRepository {
    IUsuarioRepository criarUsuarioRepository();
    IProjetoRepository criarProjetoRepository();
    IPerfilRepository criarPerfilRepository();
    IProjetoHasPerfilRepository criarProjetoPerfilRepository();
    IPerfilHasCampoRepository criarPerfilCampoRepository();
    IUsuarioHasProjetoRepository criarUsuarioProjetoRepository();
    IEstimativaHasCampoRepository criarEstimativaCampoRepository();
}
