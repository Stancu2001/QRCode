package com.example.licenta.service;

import com.example.licenta.dto.*;
import com.example.licenta.entities.*;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.exception.UnauthorizedAccesException;
import com.example.licenta.repositories.ImmobileRepository;
import com.example.licenta.repositories.ReservationRepository;
import com.example.licenta.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImmobileServiceImpl implements ImmobileService{
    @Autowired
    ImmobileRepository immobileRepository;
    @Autowired
    RoomService roomService;
    @Autowired
    PhotoService photoService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    UserService userService;
    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public void saveImmobil(CreateImmobileDTO createImmobileDTO, List<MultipartFile> photos,int id){
        Room room=roomService.findRoom(createImmobileDTO.getRoomIds());
        Property property=propertyService.findProperty(id);
        User user=userService.getCurrentUser().get();
        if(property.getUser()!=user) {
            throw  new UnauthorizedAccesException("Nu ai acces");
        }
        Immobile createimmobile=new Immobile();
        createimmobile.setDetails(createImmobileDTO.getDetails());
        createimmobile.setRooms(room);
        createimmobile.setPrice(createImmobileDTO.getPrice());
        createimmobile.setPersons(createImmobileDTO.getPersons());
        List<Photo> photosSave= photoService.savePhoto(photos);
        createimmobile.setPhotos(photosSave);
        createimmobile.setCoverPhoto(photosSave.get(0));
        createimmobile.setProperty(property);
        createimmobile.setName(createImmobileDTO.getName());
        immobileRepository.save(createimmobile);
    }
    public static List<ImmobileDTO> toDTOList(List <Immobile> immobiles) {
        return immobiles.stream()
                .map(ImmobileDTO::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public ImmobileDetailsDTO getById(int id){
            Immobile immobile = immobileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Immobile was not found"));

            // Maparea datelor de la entitate la DTO
            ImmobileDetailsDTO dto = new ImmobileDetailsDTO();
            dto.setImmobileId(immobile.getImmobileId());
            dto.setCoverPhoto(immobile.getCoverPhoto().getPhotoName());
            dto.setPersons(immobile.getPersons());
            dto.setPrice(immobile.getPrice());
            dto.setRoom(immobile.getRooms().getName());
            dto.setName(immobile.getName());
            dto.setPhotos(immobile.getPhotos().stream()
                    .map(Photo::getPhotoName)
                    .collect(Collectors.toList()));
            dto.setDetails(immobile.getDetails());
            dto.setTitle(immobile.getProperty().getTitle());
            dto.setDescription(immobile.getProperty().getDescription());
            dto.setCountry(immobile.getProperty().getCountry());
            dto.setCity(immobile.getProperty().getCity());
            dto.setStreet(immobile.getProperty().getStreet());
            dto.setNumber(immobile.getProperty().getNumber());
            dto.setType(immobile.getProperty().getType());
            dto.setPhone1(immobile.getProperty().getPhone1());
            dto.setPhone2(immobile.getProperty().getPhone2());
            dto.setEmail(immobile.getProperty().getEmail());
            return dto;
        }


    public void updateImmobile(CreateImmobileDTO createImmobileDTO, List<MultipartFile> photos,int IdProperty, int IdImmobile){
        Property property=propertyService.findProperty(IdProperty);
        User user=userService.getCurrentUser().get();
        if(property.getUser()!=user) {
            throw  new UnauthorizedAccesException("Nu ai acces");
        }
        Immobile immobile=immobileRepository.findById(IdImmobile).orElseThrow(() -> new ResourceNotFoundException("Immobile was not found"));
        List<Immobile> immobileList=property.getImmobileList();
        if(!immobileList.contains(immobile)){
            throw  new UnauthorizedAccesException("Nu ai acces");
        }
        List<Photo> Immobilephoto=immobile.getPhotos();
        for(var photo:Immobilephoto){
            photoService.deleteFiles(photo.getPhotoId());
        }
        Room room=roomService.findRoom(createImmobileDTO.getRoomIds());
        immobile.setDetails(createImmobileDTO.getDetails());
        immobile.setRooms(room);
        immobile.setPrice(createImmobileDTO.getPrice());
        immobile.setPersons(createImmobileDTO.getPersons());
        List<Photo> photosSave= photoService.savePhoto(photos);
        immobile.setPhotos(photosSave);
        immobile.setCoverPhoto(photosSave.get(0));
        immobile.setProperty(property);
        immobileRepository.save(immobile);
    }
    @Override
    public void deleteImmobile(int IdProperty, int IdImmobile){
        Property property=propertyService.findProperty(IdProperty);
        User user=userService.getCurrentUser().get();
        if(property.getUser()!=user) {
            throw  new UnauthorizedAccesException("Nu ai acces");
        }
        Immobile immobile=immobileRepository.findById(IdImmobile).orElseThrow(() -> new ResourceNotFoundException("Immobile was not found"));
        List<Immobile> immobileList=property.getImmobileList();
        if(!immobileList.contains(immobile)){
            throw  new UnauthorizedAccesException("Nu ai acces");
        }
        List<Photo> Immobilephoto=immobile.getPhotos();
        System.out.println(Immobilephoto.size());
        for(var photo:Immobilephoto){
            photoService.deleteFiles(photo.getPhotoId());
            System.out.println("Test");
            immobileRepository.delete(immobile);
        }
        immobileList.remove(immobile);
        property.setImmobileList(immobileList);
        propertyService.saveProperty(property);
        immobileRepository.deleteById(IdImmobile);
    }

    @Override
    public List<ResponseImmobileDTO> getAllByCity(String city) {
        return null;
    }

    //    @Override
//    public List<ImmobileDTO> getAllByCity(String city){
//        List<Immobile> immobileList=immobileRepository.findByPropertyCity(city);
//        return(toDTOList(immobileList));
//    }
@Override
//public List<ResponseImmobileDTO> getAllByCity(String city) {
//    List<Immobile> immobileList = immobileRepository.findByPropertyCity(city);
//    Map<Integer, List<ImmobileResultDTO>> groupedImmobiles = immobileList.stream()
//            .map(ImmobileResultDTO::fromEntity)
//            .collect(Collectors.groupingBy(ImmobileResultDTO::getPropertyId));
//    return groupedImmobiles.entrySet().stream()
//            .map(entry -> {
//                ResponseImmobileDTO dto = new ResponseImmobileDTO();
//                dto.setId(entry.getKey());
//                dto.setType(entry.getValue().get(0).getType());
//                dto.setImmobileDTOS(entry.getValue());
//                return dto;
//            })
//            .collect(Collectors.toList());
//    }
public List<ResponseImmobileDTO> getAllByCity(String city, LocalDate checkIn, LocalDate checkOut) {
    List<Immobile> immobileList = immobileRepository.findByPropertyCity(city);

    // Filtrăm imobilele pentru a exclude cele rezervate în perioada specificată
    List<Immobile> availableImmobiles = immobileList.stream()
            .filter(immobile -> isAvailable(immobile, checkIn, checkOut))
            .collect(Collectors.toList());

    // Grupăm imobilele disponibile după `propertyId`
    Map<Integer, List<ImmobileResultDTO>> groupedImmobiles = availableImmobiles.stream()
            .map(ImmobileResultDTO::fromEntity)
            .collect(Collectors.groupingBy(ImmobileResultDTO::getPropertyId));

    // Transformăm fiecare intrare din hartă într-un `ResponseImmobileDTO`
    return groupedImmobiles.entrySet().stream()
            .map(entry -> {
                ResponseImmobileDTO dto = new ResponseImmobileDTO();
                dto.setId(entry.getKey());
                dto.setType(entry.getValue().get(0).getType());
                dto.setImmobileDTOS(entry.getValue());
                return dto;
            })
            .collect(Collectors.toList());
}

    private boolean isAvailable(Immobile immobile, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> reservations = reservationRepository.findByImmobileAndDateRange(immobile, checkIn, checkOut);
        return reservations.isEmpty();
    }
    @Override
    public List<ImmobileDTO> getAll(){
        Optional<User> user=userService.getCurrentUser();
        User currentUser=user.get();
        List<Immobile> immobileList=immobileRepository.findByPropertyUserUserId(currentUser.getUserId());
        return(toDTOList(immobileList));

    }

    @Override
    public List<ImmobileNameDTO> getAllName(){
        List<Immobile> immobileList=immobileRepository.findAll();
        return immobileList.stream()
                .map(this::convertToImmobileNameDTO)
                .collect(Collectors.toList());
    }

    private ImmobileNameDTO convertToImmobileNameDTO(Immobile immobile) {
        ImmobileNameDTO dto = new ImmobileNameDTO();
        dto.setId(immobile.getImmobileId());
        dto.setName(immobile.getName());
        return dto;
    }

}
