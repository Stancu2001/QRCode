package com.example.licenta.service;

import com.example.licenta.entities.Photo;
import com.google.zxing.WriterException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PhotoService {
    List<Photo> savePhoto(List<MultipartFile> files);
    void deleteFiles(int id);
    Photo generateQRCode(UUID uuid, int id) throws WriterException, IOException;
}
