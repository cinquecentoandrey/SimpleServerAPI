package com.cinquecento.simpleserverapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String upload(MultipartFile file) throws IOException;
}
