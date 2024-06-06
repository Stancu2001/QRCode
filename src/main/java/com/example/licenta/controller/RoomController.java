package com.example.licenta.controller;

import com.example.licenta.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @GetMapping()
    public ResponseEntity<?> getAllRoom(){
        return ResponseEntity.ok(roomService.getAllRoom());
    }
}
