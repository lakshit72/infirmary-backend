package com.infirmary.backend.configuration.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.service.StaticService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/Profile")
@AllArgsConstructor
public class StaticFilesController {
    StaticService staticServiceImpl;

    @GetMapping(value = "/{filename}")
    ResponseEntity<byte[]> imageGet(@PathVariable String filename){
        byte[] img = staticServiceImpl.imageReturn(filename);
        return ResponseEntity.ok().contentType(filename.substring(filename.indexOf(".")+1).equals("png")?MediaType.IMAGE_PNG:MediaType.IMAGE_JPEG).body(img);
    }
}
