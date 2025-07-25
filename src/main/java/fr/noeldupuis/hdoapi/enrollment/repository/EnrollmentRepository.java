package fr.noeldupuis.hdoapi.enrollment.repository;

import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    @Query("SELECT e FROM Enrollment e WHERE e.person.id = :personId")
    Page<Enrollment> findByPersonId(@Param("personId") Long personId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.pilgrimage.id = :pilgrimageId")
    Page<Enrollment> findByPilgrimageId(@Param("pilgrimageId") Long pilgrimageId, Pageable pageable);
    
    @Query("SELECT e FROM Enrollment e WHERE e.person.id = :personId AND e.pilgrimage.id = :pilgrimageId")
    Optional<Enrollment> findByPersonIdAndPilgrimageId(@Param("personId") Long personId, @Param("pilgrimageId") Long pilgrimageId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.status = :status")
    Page<Enrollment> findByStatus(@Param("status") EnrollmentStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.pilgrimage.id = :pilgrimageId AND e.status = :status")
    long countByPilgrimageIdAndStatus(@Param("pilgrimageId") Long pilgrimageId, @Param("status") EnrollmentStatus status);
    
    boolean existsByPersonIdAndPilgrimageId(Long personId, Long pilgrimageId);
} 