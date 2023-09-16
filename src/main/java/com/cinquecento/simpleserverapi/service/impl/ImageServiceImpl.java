package com.cinquecento.simpleserverapi.service.impl;

import com.cinquecento.simpleserverapi.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.upload.path}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file) throws IOException {

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadDir + "/" + resFileName));

            System.out.println(resFileName);

            return resFileName;
        }

        return null;
    }

}
