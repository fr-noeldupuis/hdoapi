package fr.noeldupuis.hdoapi.persons.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "persons", itemRelation = "person")
@Schema(description = "Person resource with HATEOAS links")
public class PersonResource extends RepresentationModel<PersonResource> {
    
    @Schema(description = "Unique identifier of the person", example = "1")
    private Long id;
    
    @Schema(description = "First name of the person", example = "John")
    private String firstName;
    
    @Schema(description = "Last name of the person", example = "Doe")
    private String lastName;
    
    @Schema(description = "Birth date of the person", example = "1990-01-01")
    private LocalDate birthDate;
    
    public PersonResource(PersonDto personDto) {
        this.id = personDto.getId();
        this.firstName = personDto.getFirstName();
        this.lastName = personDto.getLastName();
        this.birthDate = personDto.getBirthDate();
    }
} 