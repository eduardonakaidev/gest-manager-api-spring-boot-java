package br.com.nestworld.gestmanagerFinancyProductApi.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(){
        super("Já existe uma conta registrada neste email");
    }
}
