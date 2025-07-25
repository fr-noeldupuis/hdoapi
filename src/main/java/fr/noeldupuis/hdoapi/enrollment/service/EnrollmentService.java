package fr.noeldupuis.hdoapi.enrollment.service;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.enrollment.dto.CreateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentDto;
import fr.noeldupuis.hdoapi.enrollment.dto.UpdateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;

public interface EnrollmentService {
    
    EnrollmentDto createEnrollment(CreateEnrollmentRequest request);
    
    EnrollmentDto getEnrollmentById(Long id);
    
    PagedResponse<EnrollmentDto> getAllEnrollments(int page, int size);
    
    PagedResponse<EnrollmentDto> getEnrollmentsByPersonId(Long personId, int page, int size);
    
    PagedResponse<EnrollmentDto> getEnrollmentsByPilgrimageId(Long pilgrimageId, int page, int size);
    
    PagedResponse<EnrollmentDto> getEnrollmentsByStatus(Enrollment.EnrollmentStatus status, int page, int size);
    
    EnrollmentDto updateEnrollment(Long id, UpdateEnrollmentRequest request);
    
    void deleteEnrollment(Long id);
    
    boolean existsByPersonIdAndPilgrimageId(Long personId, Long pilgrimageId);
    
    long countByPilgrimageIdAndStatus(Long pilgrimageId, Enrollment.EnrollmentStatus status);
} 