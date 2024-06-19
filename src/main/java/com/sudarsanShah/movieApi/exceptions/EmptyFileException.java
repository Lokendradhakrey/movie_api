package com.sudarsanShah.movieApi.exceptions;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message){
        super(message);
    }
}
