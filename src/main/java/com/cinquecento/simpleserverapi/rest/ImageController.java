package com.cinquecento.simpleserverapi.rest;

import com.cinquecento.simpleserverapi.service.ImageService;
import com.cinquecento.simpleserverapi.util.exception.UserNotFoundException;
import com.cinquecento.simpleserverapi.util.response.UserErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    @Order
    public ResponseEntity<String> upload(@RequestParam(name = "image")MultipartFile file) throws IOException {
        String uploadImage = imageService.upload(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(IOException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
