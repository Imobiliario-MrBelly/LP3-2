package br.ifsudeste.mrbellyapi.api.exception;


public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha inválida");
    }
}