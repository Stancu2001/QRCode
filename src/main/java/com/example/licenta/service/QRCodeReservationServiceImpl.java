package com.example.licenta.service;

import com.example.licenta.repositories.QRCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeReservationServiceImpl implements QRCodeReservationService {
    @Autowired
    QRCodeRepository qrCodeRepository;

    @Override
    public com.example.licenta.entities.QRCodeReservation save(com.example.licenta.entities.QRCodeReservation qrCodeReservation){
        return qrCodeRepository.save(qrCodeReservation);
    }
}
