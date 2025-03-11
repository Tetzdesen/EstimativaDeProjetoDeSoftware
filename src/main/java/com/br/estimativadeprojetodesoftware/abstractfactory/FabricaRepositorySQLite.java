package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.ICampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.repository.sqlite.CampoRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.PerfilRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.ProjetoRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.UsuarioRepositorySQLite;

/**
 *
 * @author tetzner
 */
public class FabricaRepositorySQLite implements FabricaRepository {

    @Override
    public IUsuarioRepository criarUsuarioRepository() {
        return new UsuarioRepositorySQLite();
    }

    @Override
    public IProjetoRepository criarProjetoRepository() {
        return new ProjetoRepositorySQLite();
    }

    @Override
    public IPerfilRepository criarPerfilRepository() {
        return new PerfilRepositorySQLite();
    }

    @Override
    public ICampoRepository criarCampoRepository() {
        return new CampoRepositorySQLite();
    }

}
