package org.example.final_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.final_project.dto.validator.Email;
import org.example.final_project.dto.validator.PhoneNumber;

@Setter
@Getter
public class AddressDTO {
    private String apartmentNumber;
    private String floor;
    private String building;
    private String streetNumber;
    private String street;
    private String city;
    private String country;
    private Integer addressType;

}
