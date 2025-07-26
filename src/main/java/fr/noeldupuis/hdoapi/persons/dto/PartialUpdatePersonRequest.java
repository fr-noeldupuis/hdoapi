package fr.noeldupuis.hdoapi.persons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Only include non-null fields
public class PartialUpdatePersonRequest {
    
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    
    // Helper method to check if any field is provided
    public boolean hasAnyField() {
        return firstName != null || lastName != null || birthDate != null;
    }
} 