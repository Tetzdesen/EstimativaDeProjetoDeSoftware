package com.br.estimativadeprojetodesoftware.command.database;

import com.br.estimativadeprojetodesoftware.singleton.ConexaoSingleton;
import java.sql.Connection;

/**
 *
 * @author tetzner
 */
public class CriarTabelasH2DatabaseCommand implements DatabaseCommand {
    @Override
    public void execute() {
        Connection conexao = ConexaoSingleton.getInstancia().getConexao();
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
