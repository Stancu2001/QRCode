package com.example.licenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Immobile {

    @Column(name = "immobile_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int immobileId;

    @Column(name = "name")
    @NotNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_photo_id", referencedColumnName = "photo_id")
    private Photo coverPhoto;

    @Column(name = "persons")
    private int persons;

    @Column(name = "price")
    private int price;


    @ManyToOne()
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    @NotNull
    private Room rooms;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "immobiles_files",
            joinColumns = @JoinColumn(name = "immobile_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", nullable = false)
    @JsonIgnore
    private Property property;

    @OneToMany(mappedBy = "immobile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();
}
