package com.example.licenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="QRCode_id")
    private int id;

    @Column(name = "key_entry")
    @NotNull
    private UUID key;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
