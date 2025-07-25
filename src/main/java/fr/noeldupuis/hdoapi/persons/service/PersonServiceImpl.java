package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    
    private final PersonRepository personRepository;
    
    @Override
    public Page<PersonDto> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    @Override
    public Optional<PersonDto> getPersonById(Long id) {
        return personRepository.findById(id)
                .map(this::convertToDto);
    }
    
    @Override
    public PersonDto createPerson(CreatePersonRequest request) {
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setBirthDate(request.getBirthDate());
        
        Person savedPerson = personRepository.save(person);
        return convertToDto(savedPerson);
    }
    
    @Override
    public Optional<PersonDto> updatePerson(Long id, UpdatePersonRequest request) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(request.getFirstName());
                    person.setLastName(request.getLastName());
                    person.setBirthDate(request.getBirthDate());
                    
                    Person updatedPerson = personRepository.save(person);
                    return convertToDto(updatedPerson);
                });
    }
    
    @Override
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private PersonDto convertToDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate()
        );
    }
} 