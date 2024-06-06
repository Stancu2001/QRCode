package com.example.licenta.service;

import com.example.licenta.entities.Room;

import java.util.List;

public interface RoomService {
     List<Room> getAllRoom();
     Room findRoom(int id);
}
