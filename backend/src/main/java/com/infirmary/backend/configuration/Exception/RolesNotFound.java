package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class RolesNotFound extends ResourceNotFoundException{
    public RolesNotFound(String msg){super(msg);}
}
