package com.infirmary.backend.configuration.Exception;

public class UserAlreadyExists extends IllegalArgumentException{
    public UserAlreadyExists(String msg){super(msg);}
}
