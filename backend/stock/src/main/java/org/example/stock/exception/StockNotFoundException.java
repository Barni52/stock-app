package org.example.stock.exception;

public class StockNotFoundException extends Exception{
    public StockNotFoundException(String message){
        super(message);
    }
}
