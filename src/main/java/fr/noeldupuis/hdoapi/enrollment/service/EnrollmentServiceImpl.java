package fr.noeldupuis.hdoapi.enrollment.service;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.enrollment.dto.CreateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentDto;
import fr.noeldupuis.hdoapi.enrollment.dto.UpdateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.enrollment.repository.EnrollmentRepository;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import fr.noeldupuis.hdoapi.pilgrimage.repository.PilgrimageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final PersonRepository personRepository;
    private final PilgrimageRepository pilgrimageRepository;
    
    @Override
    public EnrollmentDto createEnrollment(CreateEnrollmentRequest request) {
        // Check if person exists
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + request.getPersonId()));
        
        // Check if pilgrimage exists
        Pilgrimage pilgrimage = pilgrimageRepository.findById(request.getPilgrimageId())
                .orElseThrow(() -> new RuntimeException("Pilgrimage not found with id: " + request.getPilgrimageId()));
        
        // Check if enrollment already exists
        if (enrollmentRepository.existsByPersonIdAndPilgrimageId(request.getPersonId(), request.getPilgrimageId())) {
            throw new RuntimeException("Enrollment already exists for this person and pilgrimage");
        }
        
        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setPerson(person);
        enrollment.setPilgrimage(pilgrimage);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.PENDING);
        enrollment.setNotes(request.getNotes());
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(savedEnrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EnrollmentDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        return convertToDto(enrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto> getAllEnrollments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(pageable);
        
        List<EnrollmentDto> enrollments = enrollmentPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(enrollments, enrollmentPage, "/api/enrollments");
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto> getEnrollmentsByPersonId(Long personId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.findByPersonId(personId, pageable);
        
        List<EnrollmentDto> enrollments = enrollmentPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(enrollments, enrollmentPage, "/api/enrollments/person/" + personId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto> getEnrollmentsByPilgrimageId(Long pilgrimageId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.findByPilgrimageId(pilgrimageId, pageable);
        
        List<EnrollmentDto> enrollments = enrollmentPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(enrollments, enrollmentPage, "/api/enrollments/pilgrimage/" + pilgrimageId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentDto> getEnrollmentsByStatus(Enrollment.EnrollmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentRepository.findByStatus(status, pageable);
        
        List<EnrollmentDto> enrollments = enrollmentPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(enrollments, enrollmentPage, "/api/enrollments/status/" + status);
    }
    
    @Override
    public EnrollmentDto updateEnrollment(Long id, UpdateEnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }
        
        if (request.getNotes() != null) {
            enrollment.setNotes(request.getNotes());
        }
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(updatedEnrollment);
    }
    
    @Override
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByPersonIdAndPilgrimageId(Long personId, Long pilgrimageId) {
        return enrollmentRepository.existsByPersonIdAndPilgrimageId(personId, pilgrimageId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByPilgrimageIdAndStatus(Long pilgrimageId, Enrollment.EnrollmentStatus status) {
        return enrollmentRepository.countByPilgrimageIdAndStatus(pilgrimageId, status);
    }
    
    private EnrollmentDto convertToDto(Enrollment enrollment) {
        return new EnrollmentDto(
                enrollment.getId(),
                enrollment.getPerson().getId(),
                enrollment.getPerson().getFirstName() + " " + enrollment.getPerson().getLastName(),
                enrollment.getPilgrimage().getId(),
                enrollment.getPilgrimage().getName(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus(),
                enrollment.getNotes()
        );
    }
} 