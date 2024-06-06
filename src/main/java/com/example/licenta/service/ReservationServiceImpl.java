package com.example.licenta.service;

import com.example.licenta.dto.*;
import com.example.licenta.entities.*;
import com.example.licenta.entities.QRCodeReservation;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.exception.UnauthorizedAccesException;
import com.example.licenta.repositories.ImmobileRepository;
import com.example.licenta.repositories.ReservationRepository;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ImmobileRepository immobileRepository;
    @Autowired
    UserService userService;
    @Autowired
    PhotoService photoService;
    @Autowired
    QRCodeReservationService qrCodeReservationService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void createReservation(CreateReservationDTO createReservationDTO) throws IOException, WriterException, MessagingException {
        Immobile immobile = immobileRepository.findById(createReservationDTO.getImmobileId()).orElseThrow(() -> new ResourceNotFoundException("Immobile was not found"));
        User user = userService.getCurrentUser().get();
        boolean isReserved = reservationRepository.existsReservationInDateRange(immobile, createReservationDTO.getCheckIn(), createReservationDTO.getCheckOut());
        if (isReserved) {
            throw new UnauthorizedAccesException("");
        }
        Reservation reservation = new Reservation();
        reservation.setCheckIn(createReservationDTO.getCheckIn());
        reservation.setCheckOut(createReservationDTO.getCheckOut());
        reservation.setImmobile(immobile);
        reservation.setTotalPrice(createReservationDTO.getTotalprice());
        reservation.setUser(user);
        reservation.setCreatedAt(LocalDate.now());
        reservation.setCanceled(false);
        reservation.setExpired(false);


        var qrcode = new QRCodeReservation();
        UUID uuid = UUID.randomUUID();
        qrcode.setKey(uuid);
        System.out.println(uuid);
        Photo photo = photoService.generateQRCode(uuid, reservation.getId());
        qrcode.setPhoto(photo);
        qrcode = qrCodeReservationService.save(qrcode);
        reservation.setQrCodeReservations(qrcode);
        reservation = reservationRepository.save(reservation);
        sendOwnerEMail(reservation.getImmobile().getName(), reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName(), reservation.getImmobile().getProperty().getEmail());

        String immobileName = reservation.getImmobile().getName();
        String userEmail = reservation.getUser().getEmail();
        String userName = reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName();
        String propertyName = reservation.getImmobile().getProperty().getTitle();
        String propertyAddress = reservation.getImmobile().getProperty().getCountry() + " " + reservation.getImmobile().getProperty().getCity() + " " +
                reservation.getImmobile().getProperty().getStreet() + " " + reservation.getImmobile().getProperty().getNumber();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String checkIn = reservation.getCheckIn().format(formatter);
        String checkOut = reservation.getCheckOut().format(formatter);
        String phone1 = reservation.getImmobile().getProperty().getPhone1();
        String phone2 = reservation.getImmobile().getProperty().getPhone2();
        String email = reservation.getImmobile().getProperty().getEmail();
        System.out.println(reservation.getQrCodeReservations().getId()+" "+reservation.getQrCodeReservations().getPhoto().getOriginalPhotoName()+" "+reservation.getQrCodeReservations().getKey());
        String QRName=qrcode.getPhoto().getOriginalPhotoName();

        senderRenterEmail(immobileName, userEmail, userName, propertyName, propertyAddress, checkIn, checkOut, phone1, phone2, email,QRName);

    }

    public void sendOwnerEMail(String immobileName, String renterName, String ownerEmail) throws MessagingException {
        Context context = new Context();
        context.setVariable("immobileName", immobileName);
        context.setVariable("renterName", renterName);

        String process = templateEngine.process("proprietar-template.html", context);

        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage());
        message.setFrom(senderEmail);
        message.setTo(ownerEmail);
        message.setSubject("Imobil Inchiriat");
        message.setText(process, true);

        javaMailSender.send(message.getMimeMessage());
    }

    public void senderRenterEmail(String immobileName, String renterEmail, String renterName, String propertyName, String propertyAddress, String checkInDate,
                                  String checkOutDate, String contactPhone1, String contactPhone2, String contactEmail, String QRName) throws MessagingException {
        Context context = new Context();
        context.setVariable("renterName", renterName);
        context.setVariable("immobileName", immobileName);
        context.setVariable("propertyName", propertyName);
        context.setVariable("propertyAddress", propertyAddress);
        context.setVariable("checkInDate", checkInDate);
        context.setVariable("checkOutDate", checkOutDate);
        context.setVariable("contactPhone1", contactPhone1);
        context.setVariable("contactPhone2", contactPhone2);
        context.setVariable("contactEmail", contactEmail);
        context.setVariable("QRName", QRName);

        String process = templateEngine.process("user-template.html", context);

        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage());
        message.setFrom(senderEmail);
        message.setTo(renterEmail);
        message.setSubject("Imobil Inchiriat");
        message.setText(process, true);
        javaMailSender.send(message.getMimeMessage());
    }
    @Override
    public List<ReservationUserDTO> getAllReservationUser(){
        User user=userService.getCurrentUser().get();
        List<Reservation> reservations=reservationRepository.findByUserUserId(user.getUserId());
        return reservations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ReservationUserDTO convertToDto(Reservation reservation) {
        return new ReservationUserDTO(
                reservation.getId(),
                reservation.getImmobile().getName(),
                reservation.getImmobile().getImmobileId(),
                reservation.getCheckIn().format(DATE_FORMATTER),
                reservation.getCheckOut().format(DATE_FORMATTER),
                reservation.getTotalPrice(),
                reservation.getCreatedAt().format(DATE_FORMATTER),
                reservation.isExpired(),
                reservation.isCanceled(),
                reservation.getQrCodeReservations().getPhoto().getPhotoName()
        );
    }
    @Override
    public List<OwnerReservation> findOwnerReservations() {
        User user=userService.getCurrentUser().get();
        List<Immobile> immobiles = immobileRepository.findByPropertyUserUserId(user.getUserId());

        // Convertește fiecare imobil într-un OwnerReservation
        return immobiles.stream().map(this::convertToOwnerReservation).collect(Collectors.toList());
    }

    private OwnerReservation convertToOwnerReservation(Immobile immobile) {
        // Obține rezervările pentru fiecare imobil
        List<ReservationDetailsDTO> reservationDetailsDTOList = immobile.getReservations().stream()
                .map(this::convertToReservationDetailsDTO)
                .collect(Collectors.toList());

        // Construieste obiectul OwnerReservation
        return new OwnerReservation(immobile.getImmobileId(), immobile.getName(), reservationDetailsDTOList);
    }

    private ReservationDetailsDTO convertToReservationDetailsDTO(Reservation reservation) {
        // Convertește obiectul Reservation în ReservationDetailsDTO
        return new ReservationDetailsDTO(
                reservation.getId(),
                reservation.getCheckIn().format(DATE_FORMATTER),
                reservation.getCheckOut().format(DATE_FORMATTER),
                reservation.getUser().getFirstName()+" "+reservation.getUser().getLastName(),
                reservation.getTotalPrice(),
                reservation.getCreatedAt().format(DATE_FORMATTER),
                reservation.isExpired(),
                reservation.isCanceled()
        );
    }
    @Override
    public int cancelReservation(int id){
        try {
            User user=userService.getCurrentUser().get();
            Reservation reservation=reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation was not found"));
            if(user!=reservation.getUser()){
                return 4;
            }
            if(reservation.isCanceled()){
                return 8;
            }
            if(reservation.isExpired()){
                return 16;
            }
            reservation.setCanceled(true);
            sendOwnerEMail(reservation);
            senderRenterEmail(reservation);

            reservationRepository.save(reservation);
            return 2;
        }
        catch (Exception e){
            return 1;
        }
    }
    public void sendOwnerEMail(Reservation reservation) throws MessagingException {
        Context context = new Context();
        context.setVariable("immobileName", reservation.getImmobile().getName());
        context.setVariable("propertyName",reservation.getImmobile().getProperty().getTitle());

        String process = templateEngine.process("proprietar-anulare-template.html", context);

        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage());
        message.setFrom(senderEmail);
        message.setTo(reservation.getImmobile().getProperty().getUser().getEmail());
        message.setSubject("Imobil Inchiriat");
        message.setText(process, true);

        javaMailSender.send(message.getMimeMessage());
    }
    public void senderRenterEmail(Reservation reservation) throws MessagingException {
        Context context = new Context();
        context.setVariable("renterName", reservation.getUser().getFirstName()+" "+reservation.getUser().getLastName());
        context.setVariable("immobileName", reservation.getImmobile().getName());

        String process = templateEngine.process("user-anulare-template.html", context);

        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage());
        message.setFrom(senderEmail);
        message.setTo(reservation.getUser().getEmail());
        message.setSubject("Imobil Inchiriat");
        message.setText(process, true);
        javaMailSender.send(message.getMimeMessage());
    }
    @Override
    public boolean checkQrCode(QrCodeDTO codeDTO){
        UUID uuid=UUID.fromString(codeDTO.getCode());
        Reservation reservation=reservationRepository.findByQrCodeReservationsKey(uuid).orElseThrow(() -> new ResourceNotFoundException("Reservation was not found"));
        System.out.println(reservation.getId());
        System.out.println(reservation.getQrCodeReservations().getKey());
        if(reservation.isExpired()||reservation.isCanceled()){
            return false;
        }
        return reservation.getImmobile().getImmobileId() == codeDTO.getId();
    }
}
