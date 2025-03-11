package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.ICampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.repository.h2.CampoRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.PerfilRepositoryH2;
import com.br.estimativadeprojetodesoftware.repository.h2.ProjetoRepositoryH2;
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
    public ICampoRepository criarCampoRepository() {
        return new CampoRepositoryH2();
    }

}
