package com.example.licenta.service;



import com.example.licenta.entities.Photo;
import com.example.licenta.repositories.PhotoRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    PhotoRepository photorepository;
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public List<Photo> savePhoto(List<MultipartFile> files){
        List<Photo> url = new ArrayList<>();

        for (MultipartFile file : files) {
            String uniqueFilename = "";
            try {
                if (!new File(uploadPath).exists()) {
                    new File(uploadPath).mkdirs();
                }
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                System.out.println(originalFileName);
                uniqueFilename = UUID.randomUUID() + originalFileName;

                Path filePath = Path.of(uploadPath, uniqueFilename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                Photo savePhoto = new Photo();
                savePhoto.setPhotoName(uniqueFilename);
                savePhoto.setOriginalPhotoName(originalFileName);
                savePhoto.setCreatedDate(LocalDateTime.now());
                savePhoto= photorepository.save(savePhoto);
//                System.out.println(savePhoto);
//                String url1=originalFileName+" poate vizualizat la: http://localhost:8080/uploads/files/"+uniqueFilename;
                url.add(savePhoto);
            } catch (IOException e) {

            }
        }
        return url;
    }
    @Override
    public void deleteFiles(int id) {
        if (photorepository.existsById(id)) {
            Photo photo = photorepository.findById(id).orElse(null);
            if (photo != null) {
                String name = photo.getPhotoName();
                String filePath = uploadPath + File.separator + name;
                File fileToDelete = new File(filePath);
                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        System.out.println("Deleted file: " + filePath);
                        photorepository.deleteById(id);
                        return;
                    } else {
                        System.err.println("Failed to delete file: " + filePath);
                    }
                } else {
                    System.err.println("File does not exist: " + filePath);
                }
            }
        }
    }
    @Override
    public Photo generateQRCode(UUID uuid, int id) throws WriterException, IOException {
        String uuidString = uuid.toString();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(uuidString, BarcodeFormat.QR_CODE, 400, 400);

        String qrCodeFileName = uuidString + "-QRCode.png";
        Path qrCodeFilePath = FileSystems.getDefault().getPath(uploadPath, qrCodeFileName);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodeFilePath);

        System.out.println("QR Code generated and saved at: " + qrCodeFilePath.toString());

        // Creăm un obiect Photo și îl populăm cu informațiile necesare
        Photo photo = new Photo();
        photo.setOriginalPhotoName(qrCodeFileName);
        photo.setPhotoName(qrCodeFileName);
        photo.setCreatedDate(LocalDateTime.now());
        photo=photorepository.save(photo);
        // Returnăm obiectul Photo
        return photo;
    }
}
