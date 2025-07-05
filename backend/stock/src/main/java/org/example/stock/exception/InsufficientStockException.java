package org.example.stock.exception;

public class InsufficientStockException extends Exception{
    public InsufficientStockException(String message){
        super(message);
    }
}
