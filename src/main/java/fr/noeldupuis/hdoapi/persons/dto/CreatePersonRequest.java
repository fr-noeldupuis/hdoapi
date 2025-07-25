package fr.noeldupuis.hdoapi.persons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new person")
public class CreatePersonRequest {
    
    @Schema(description = "First name of the person", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the person", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Birth date of the person", example = "1990-01-01", required = true)
    private LocalDate birthDate;
} 