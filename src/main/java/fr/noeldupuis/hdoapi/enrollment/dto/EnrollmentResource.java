package fr.noeldupuis.hdoapi.enrollment.dto;

import org.springframework.hateoas.RepresentationModel;

public class EnrollmentResource extends RepresentationModel<EnrollmentResource> {
    
    private final EnrollmentDto enrollment;
    
    public EnrollmentResource(EnrollmentDto enrollment) {
        this.enrollment = enrollment;
    }
    
    public EnrollmentDto getEnrollment() {
        return enrollment;
    }
} 