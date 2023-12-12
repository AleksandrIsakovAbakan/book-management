package com.example.bookmanagement.exception;

public class AlreadySuchNameException extends RuntimeException{

    public AlreadySuchNameException(String errorMessage) {
        super(errorMessage);
    }
}
