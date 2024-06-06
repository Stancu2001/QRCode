package com.example.licenta.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "\"files\"")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private int photoId;

    @Column(name = "original_photo_name", nullable = false)
    private String originalPhotoName;

    @Column(name = "photo_name", nullable = false)
    private String photoName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}

