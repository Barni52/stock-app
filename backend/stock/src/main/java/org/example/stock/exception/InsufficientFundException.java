package org.example.stock.exception;

public class InsufficientFundException extends Exception{
    public InsufficientFundException(String message){
        super(message);
    }
}
