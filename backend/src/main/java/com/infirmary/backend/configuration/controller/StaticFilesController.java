package com.infirmary.backend.configuration.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.service.StaticService;

@RestController
@RequestMapping(value = "/Profile")
public class StaticFilesController {
    StaticService staticServiceImpl;

    @GetMapping(value = "/{filename}")
    ResponseEntity<byte[]> imageGet(@PathVariable String filename){
        byte[] img = staticServiceImpl.imageReturn(filename);
        String type = filename.split(".")[1];
        return ResponseEntity.ok().contentType(type.equals("png")? MediaType.IMAGE_PNG:MediaType.IMAGE_JPEG).body(img);
    }
}
