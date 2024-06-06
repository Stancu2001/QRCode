package com.example.licenta.controller;

import com.example.licenta.dto.CreateReservationDTO;
import com.example.licenta.dto.OwnerReservation;
import com.example.licenta.dto.QrCodeDTO;
import com.example.licenta.dto.ReservationUserDTO;
import com.example.licenta.entities.Reservation;
import com.example.licenta.repositories.ReservationRepository;
import com.example.licenta.service.ReservationService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save")
    public ResponseEntity<?> saveReservation(@Valid @RequestBody CreateReservationDTO createReservationDTO) throws IOException, WriterException, MessagingException {
        reservationService.createReservation(createReservationDTO);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/userReservations")
    public ResponseEntity<?> getAllUserReservations(){
        List<ReservationUserDTO> reservationUserDTOList=reservationService.getAllReservationUser();
        return ResponseEntity.ok(reservationUserDTOList);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ownerReservations")
    public ResponseEntity<?> getAllOwnerReservations(){
        List<OwnerReservation> ownerReservations=reservationService.findOwnerReservations();
        return ResponseEntity.ok(ownerReservations);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable int id){
        int isCanceled= reservationService.cancelReservation(id);
        return switch (isCanceled) {
            case 2 -> ResponseEntity.ok().build();
            case 4, 8, 16-> ResponseEntity.status(401).build();
            default -> ResponseEntity.internalServerError().build();
        };
    }
    @PostMapping()
    public ResponseEntity<?> checkQrCode(@RequestBody QrCodeDTO qrCodeDTO){
        System.out.println(qrCodeDTO.getCode()+" "+qrCodeDTO.getId());

        boolean check=reservationService.checkQrCode(qrCodeDTO);
        if(!check) {
            System.out.println("a");
            return ResponseEntity.status(401).build();
        }
        System.out.println("b");
        return ResponseEntity.ok().build();
    }
}
