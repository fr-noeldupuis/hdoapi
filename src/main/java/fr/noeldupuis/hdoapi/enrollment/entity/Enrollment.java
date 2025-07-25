package fr.noeldupuis.hdoapi.enrollment.entity;

import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilgrimage_id", nullable = false)
    private Pilgrimage pilgrimage;
    
    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;
    
    @Column(name = "notes")
    private String notes;
    
    public enum EnrollmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
} 