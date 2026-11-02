package com.github.tetzdesen.estimativalog;

public class LogException extends RuntimeException {

    public LogException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
