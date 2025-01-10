package com.example.movieAPI.exceptions;

import java.io.IOException;

public class EmptyFileException extends Throwable {
    public EmptyFileException(String message){
        super(message);
    }
}
