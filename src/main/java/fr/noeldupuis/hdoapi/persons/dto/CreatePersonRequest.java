package fr.noeldupuis.hdoapi.persons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
} 