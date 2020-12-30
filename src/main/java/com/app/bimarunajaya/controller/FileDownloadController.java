package com.app.bimarunajaya.controller;

import com.app.bimarunajaya.service.BaseService;
import com.vaadin.flow.component.UI;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

    @GetMapping
    public ResponseEntity<InputStreamResource> download(@Param("code") String code) {
        String path = BaseService.pathQRAddr + code + ".PNG";

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(path);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/exc")
    public ResponseEntity<InputStreamResource> downloadExc(@Param("code") String code) {
        String path = BaseService.pathQR + code;

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(path);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
