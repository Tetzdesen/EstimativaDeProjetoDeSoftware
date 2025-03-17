package com.br.estimativadeprojetodesoftware.service;

import com.br.log.ILog;

/**
 *
 * @author tetzner
 */
public class LogService {

    private static LogService instancia;
    private ILog log;

    private LogService(){
        
    }
    
    public static LogService getInstancia(){
        if(instancia == null){
            instancia = new LogService();
        }
        return instancia;
    }
    
    public void escreverMensagem(String mensagem){
        if(mensagem.isEmpty()){
            throw new IllegalArgumentException("Erro: Mensagem vazia");
        }
        log.escrever(mensagem);
    }
    
    public ILog getLog() {
        return log;
    }

    public void setLog(ILog log) {
        if (log == null) {
            throw new IllegalArgumentException("Erro: opção de log vazia");
        }
        this.log = log;
    }
}
