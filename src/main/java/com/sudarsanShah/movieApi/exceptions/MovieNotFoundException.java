package com.sudarsanShah.movieApi.exceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message){
        super(message);
    }
}
