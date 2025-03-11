package com.br.estimativadeprojetodesoftware.abstractfactory;

import com.br.estimativadeprojetodesoftware.repository.IEstimativaHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilHasCampoRepository;
import com.br.estimativadeprojetodesoftware.repository.IPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoHasPerfilRepository;
import com.br.estimativadeprojetodesoftware.repository.IProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioHasProjetoRepository;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.repository.sqlite.EstimativaHasCampoRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.PerfilHasCampoRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.PerfilRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.ProjetoHasPerfilRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.ProjetoRepositorySQLite;
import com.br.estimativadeprojetodesoftware.repository.sqlite.UsuarioHasProjetoRepositorySQLite;
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
    public IProjetoHasPerfilRepository criarProjetoPerfilRepository() {
        return new ProjetoHasPerfilRepositorySQLite();
    }

    @Override
    public IPerfilHasCampoRepository criarPerfilCampoRepository() {
        return new PerfilHasCampoRepositorySQLite();
    }

    @Override
    public IUsuarioHasProjetoRepository criarUsuarioProjetoRepository() {
        return new UsuarioHasProjetoRepositorySQLite();
    }

    @Override
    public IEstimativaHasCampoRepository criarEstimativaCampoRepository() {
        return new EstimativaHasCampoRepositorySQLite();
    }

}
