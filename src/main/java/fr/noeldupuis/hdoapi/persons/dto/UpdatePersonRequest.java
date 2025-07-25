package fr.noeldupuis.hdoapi.persons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing person")
public class UpdatePersonRequest {
    
    @Schema(description = "First name of the person", example = "John")
    private String firstName;
    
    @Schema(description = "Last name of the person", example = "Doe")
    private String lastName;
    
    @Schema(description = "Birth date of the person", example = "1990-01-01")
    private LocalDate birthDate;
} 