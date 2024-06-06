package com.example.licenta.service;

import com.example.licenta.dto.CreatePropertyDTO;
import com.example.licenta.dto.PredictiveSuggestionDTO;
import com.example.licenta.dto.PropertyDTO;
import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Photo;
import com.example.licenta.entities.Property;
import com.example.licenta.entities.User;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.exception.UnauthorizedAccesException;
import com.example.licenta.repositories.PropertyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PhotoService photoService;

    @Override
    public void saveProperty(Property property){
        propertyRepository.save(property);
    }

    public void deleteProperty(Property property){ propertyRepository.delete(property);}

    @Override
    public boolean saveProperty(CreatePropertyDTO createPropertyDTO){
        try {
            System.out.println("a");
            Property createproperty = modelMapper.map(createPropertyDTO, Property.class);
            System.out.println("b");
//            User user = userService.getCurrentUser().get();
            User user = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User was not found"));
            System.out.println(user);
            System.out.println(user.getUserId());
            System.out.println("c");
            createproperty.setUser(user);
            System.out.println("d");
            createproperty.setAddDate(LocalDate.now());
            System.out.println("e");
            saveProperty(createproperty);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PropertyDTO> getUserProperty(){
        User user = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        List<Property> propertyList=propertyRepository.findByUserUserId(user.getUserId());
        return propertyList.stream().map(property -> modelMapper.map(property, PropertyDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean updateProperty(int id,CreatePropertyDTO createPropertyDTO){
        User user = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        Property property= propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property was not found"));
        if(user!=property.getUser()){
            throw new UnauthorizedAccesException("Current user is not authorized.");
        }
        property.setTitle(createPropertyDTO.getTitle());
        property.setDescription(createPropertyDTO.getDescription());
        property.setCountry(createPropertyDTO.getCountry());
        property.setCity(createPropertyDTO.getCity());
        property.setStreet(createPropertyDTO.getStreet());
        property.setNumber(createPropertyDTO.getNumber());
        property.setPhone1(createPropertyDTO.getPhone1());
        property.setPhone2(createPropertyDTO.getPhone2());
        property.setEmail(createPropertyDTO.getEmail());
        saveProperty(property);
        return true;
    }


    @Override
    public boolean deleteProperty(int id){
        User user = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User was not found"));
        Property property= propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property was not found"));
        if(user!=property.getUser()){
            throw new UnauthorizedAccesException("Current user is not authorized.");
        }
        List<Immobile> immobileList=property.getImmobileList();
        for(var immobile:immobileList){
            List<Photo> photos=immobile.getPhotos();
            for(var photo:photos){
                photoService.deleteFiles(photo.getPhotoId());
            }
        }
        deleteProperty(property);
        return true;
    }
    @Override
    public Property findProperty(int id){
        return propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property was not found"));
    }
    @Override
    public List<PredictiveSuggestionDTO> getDistinctCityDTOs(){
        return propertyRepository.findDistinctCityDTOs();
    }
}
