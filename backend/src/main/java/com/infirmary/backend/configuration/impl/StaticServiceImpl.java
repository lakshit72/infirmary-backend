package com.infirmary.backend.configuration.impl;

import java.io.File;
import java.lang.String;

import org.apache.commons.io.FileUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.service.StaticService;

import lombok.NoArgsConstructor;

import java.io.IOException;

@Service
@NoArgsConstructor
public class StaticServiceImpl implements StaticService{

    private String filePath = "./build/resources/main/static/Profile";

    @Override
    public byte[] imageReturn(String filename) {
        byte [] image = new byte[0];
        try{
            image = FileUtils.readFileToByteArray(new File(filePath+"/"+filename));
        }catch(IOException err){
            err.printStackTrace();
            throw new ResourceNotFoundException("Image Not Found");
        }
        return image;
    }
    
}
