package com.drufontael.todolist.exceptions;

public class NumberCharExceededException extends RuntimeException{
    public NumberCharExceededException(String message) {
        super(message);
    }
}
