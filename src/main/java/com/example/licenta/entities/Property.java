package com.example.licenta.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "\"property\"")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private int propertyId;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "add_Date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate addDate;

    @NotBlank
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank
    @Column(name = "type_property")
    private String type;

    @NotBlank
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Column(name = "number", nullable = false)
    private String number;

    @NotBlank
    @Column(name = "phone1", nullable = false)
    private String phone1;

    @NotBlank
    @Column(name = "phone2", nullable = false)
    private String phone2;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Immobile> immobileList = new ArrayList<>();
}
