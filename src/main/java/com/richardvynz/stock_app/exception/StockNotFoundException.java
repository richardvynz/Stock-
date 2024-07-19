package com.richardvynz.stock_app.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message){
        super(message);
    }

}
