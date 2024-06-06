package com.example.licenta.service;

import com.example.licenta.dto.*;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface ReservationService {
    void createReservation(CreateReservationDTO createReservationDTO) throws IOException, WriterException, MessagingException;
    List<ReservationUserDTO> getAllReservationUser();
    List<OwnerReservation> findOwnerReservations();
    int cancelReservation(int id);
    boolean checkQrCode(QrCodeDTO codeDTO);
}
