package com.example.licenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immobile_id", referencedColumnName = "immobile_id", nullable = false)
    @JsonIgnore
    private Immobile immobile;

    @Column(name="checkIn")
    @NotNull
    private LocalDate checkIn;

    @Column(name="checkOut")
    @NotNull
    private LocalDate checkOut;

    @Column(name = "totalPrice")
    @NotNull
    private int totalPrice;
    @Column(name = "created")
    @NotNull
    private LocalDate createdAt;
    @Column(name = "expired")
    private boolean isExpired;


    @Column(name = "canceled")
    private boolean isCanceled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "QRCode_id", referencedColumnName = "QRCode_id")
    private QRCodeReservation qrCodeReservations;
}
