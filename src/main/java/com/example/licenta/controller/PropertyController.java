package com.example.licenta.controller;

import com.example.licenta.dto.CreatePropertyDTO;
import com.example.licenta.dto.PredictiveSuggestionDTO;
import com.example.licenta.dto.PropertyDTO;
import com.example.licenta.entities.Property;
import com.example.licenta.entities.User;
import com.example.licenta.exception.UnauthorizedAccesException;
import com.example.licenta.repositories.PasswordResetTokenRepository;
import com.example.licenta.service.PropertyService;
import com.example.licenta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {
    @Autowired
    PropertyService propertyService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save")
    public ResponseEntity<?> saveProperty(@Valid @RequestBody CreatePropertyDTO createPropertyDTO){
        boolean saveProperty= propertyService.saveProperty(createPropertyDTO);
        if(!saveProperty){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<?> getUserProperty(){
        List<PropertyDTO> propertyList=propertyService.getUserProperty();
        PropertyDTO property=propertyList.get(0);
        System.out.println(property.getAddDate());
        return ResponseEntity.ok(propertyList);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyProperty(@PathVariable int id, @Valid @RequestBody CreatePropertyDTO createPropertyDTO){
        boolean modify=propertyService.updateProperty(id,createPropertyDTO);
        if(!modify){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable int id){
        boolean deleteProperty=propertyService.deleteProperty(id);
        if(!deleteProperty){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
    @GetMapping("/cities")
    public List<PredictiveSuggestionDTO> getDistinctCityDTOs() {
        return propertyService.getDistinctCityDTOs();
    }

}
