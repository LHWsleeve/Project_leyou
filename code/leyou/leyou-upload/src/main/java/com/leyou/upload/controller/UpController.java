package com.leyou.upload.controller;


import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/upload")
public class UpController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> upLoadImage(@RequestParam("file") MultipartFile file) {
        String url = uploadService.uploadImage(file);
        if (StringUtils.isBlank(url)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
