package com.example.licenta.dto;

import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImmobileDTO {
    int id;
    private String type;
    List<ImmobileResultDTO> immobileDTOS;
}