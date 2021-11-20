package com.conversion.base64conversion.controller;

import com.conversion.base64conversion.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @GetMapping("/file")
    public String getFile(){
        return "hello world !!!";
    }

    @PostMapping(value = "/convert/img",produces={IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] convertImage(@RequestBody String base64File) throws IOException {
        return fileService.convertImage(base64File);
        //System.out.println("Extension: "+extension);
        //System.out.println("Header: "+base64Header);
        //System.out.println("File: "+base64Image);
        //return base64File;
        //return Files.readAllBytes(Paths.get("converted."+extension));

    }
    @PostMapping(value = "/convert/pdf",produces={"application/pdf"})
    public byte[] convertPdf(@RequestBody String base64File) throws IOException {
        return fileService.convertPdf(base64File);
    }

}
