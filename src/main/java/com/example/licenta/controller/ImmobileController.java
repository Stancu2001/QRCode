package com.example.licenta.controller;

import com.example.licenta.dto.*;
import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Photo;
import com.example.licenta.entities.Property;
import com.example.licenta.entities.Room;
import com.example.licenta.repositories.ImmobileRepository;
import com.example.licenta.repositories.PropertyRepository;
import com.example.licenta.repositories.RoomRepository;
import com.example.licenta.service.ImmobileService;
import com.example.licenta.service.PhotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/immobile")
public class ImmobileController {
    @Autowired
    ImmobileService immobileService;

    @Autowired
    PhotoService fileService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}/save")
    public ResponseEntity<?> saveImmobile(@Valid @RequestPart("CreateImmobileDTO") CreateImmobileDTO createImmobileDTO, @RequestParam("Files") List<MultipartFile> photos, @PathVariable int id){
        immobileService.saveImmobil(createImmobileDTO,photos,id);

        return ResponseEntity.ok().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllImmobiles(@PathVariable int id){
        ImmobileDetailsDTO immobileDTOS=immobileService.getById(id);
        return ResponseEntity.ok(immobileDTOS);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{idProperty}/{idImmobile}")
    public ResponseEntity<?> updateImmobile(@Valid @RequestPart("CreateImmobileDTO") CreateImmobileDTO createImmobileDTO, @RequestParam("Files") List<MultipartFile> photos, @PathVariable int idProperty, @PathVariable int idImmobile){
        immobileService.updateImmobile(createImmobileDTO,photos,idProperty,idImmobile);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idProperty}/{idImmobile}")
    public ResponseEntity<?> deleteImmobile(@PathVariable int idImmobile, @PathVariable int idProperty){
        immobileService.deleteImmobile(idProperty,idImmobile);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/city")
    public ResponseEntity<?> Getall(@Valid @RequestBody SearchImmobile searchImmobile){
        List<ResponseImmobileDTO> immobileDTOS=immobileService.getAllByCity(searchImmobile.getCity(),searchImmobile.getCheckIn(),searchImmobile.getCheckOut());
        return ResponseEntity.ok(immobileDTOS);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllImmobileByUser(){
        List<ImmobileDTO> immobileDTOS=immobileService.getAll();
        return ResponseEntity.ok(immobileDTOS);
    }
    @GetMapping("/getName")
    public ResponseEntity<?> getName(){
        List<ImmobileNameDTO> immobileNameDTOS=immobileService.getAllName();
        return ResponseEntity.ok(immobileNameDTOS);
    }
}
