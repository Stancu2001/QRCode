package com.example.licenta.service;

import com.example.licenta.dto.CreateRoomDTO;
import com.example.licenta.entities.Room;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Room findRoom(int id){
        return roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room was not found"));
    }
    @Override
    public List<Room> getAllRoom(){
        return roomRepository.findAll();
    }

}
