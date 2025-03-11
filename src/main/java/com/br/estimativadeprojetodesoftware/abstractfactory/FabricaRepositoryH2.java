package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.IEstimativaHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoHasPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioHasProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.repository.h2.EstimativaHasCampoRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.PerfilHasCampoRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.PerfilRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.ProjetoHasPerfilRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.ProjetoRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.UsuarioHasProjetoRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.UsuarioRepositoryH2;

/**
 *
 * @author tetzner
 */
public class FabricaRepositoryH2 implements FabricaRepository {

    @Override
    public IUsuarioRepository criarUsuarioRepository() {
        return new UsuarioRepositoryH2();
    }

    @Override
    public IProjetoRepository criarProjetoRepository() {
        return new ProjetoRepositoryH2();
    }

    @Override
    public IPerfilRepository criarPerfilRepository() {
       return new PerfilRepositoryH2();
    }
    
    @Override
    public IProjetoHasPerfilRepository criarProjetoPerfilRepository() {
        return new ProjetoHasPerfilRepositoryH2();
    }

    @Override
    public IPerfilHasCampoRepository criarPerfilCampoRepository() {
       return new PerfilHasCampoRepositoryH2();
    }

    @Override
    public IUsuarioHasProjetoRepository criarUsuarioProjetoRepository() {
        return new UsuarioHasProjetoRepositoryH2();
    }

    @Override
    public IEstimativaHasCampoRepository criarEstimativaCampoRepository() {
        return new EstimativaHasCampoRepositoryH2();
    }

}
