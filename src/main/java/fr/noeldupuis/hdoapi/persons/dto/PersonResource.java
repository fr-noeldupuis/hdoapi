package fr.noeldupuis.hdoapi.persons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "persons", itemRelation = "person")
public class PersonResource extends RepresentationModel<PersonResource> {
    
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    
    public PersonResource(PersonDto personDto) {
        this.id = personDto.getId();
        this.firstName = personDto.getFirstName();
        this.lastName = personDto.getLastName();
        this.birthDate = personDto.getBirthDate();
    }
} 