package com.example.licenta.service;

import com.example.licenta.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ImmobileService {
    void saveImmobil(CreateImmobileDTO createImmobileDTO, List<MultipartFile> photos, int id);
    ImmobileDetailsDTO getById(int id);
    List<ImmobileDTO> getAll();
    void updateImmobile(CreateImmobileDTO createImmobileDTO, List<MultipartFile> photos,int IdProperty, int IdImmobile);
    void deleteImmobile(int IdProperty, int IdImmobile);
    List<ResponseImmobileDTO> getAllByCity(String city);
    List<ResponseImmobileDTO> getAllByCity(String city, LocalDate checkIn, LocalDate checkOut);
    List<ImmobileNameDTO> getAllName();
}
