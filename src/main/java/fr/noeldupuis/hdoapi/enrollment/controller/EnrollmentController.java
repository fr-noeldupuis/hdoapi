package fr.noeldupuis.hdoapi.enrollment.controller;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.enrollment.dto.CreateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentDto;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentResource;
import fr.noeldupuis.hdoapi.enrollment.dto.UpdateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.enrollment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment", description = "Enrollment management APIs")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping
    @Operation(summary = "Create a new enrollment", description = "Enroll a person in a pilgrimage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully",
                    content = @Content(schema = @Schema(implementation = EnrollmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Person or pilgrimage not found"),
            @ApiResponse(responseCode = "409", description = "Enrollment already exists")
    })
    public ResponseEntity<EnrollmentResource> createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request) {
        EnrollmentDto enrollment = enrollmentService.createEnrollment(request);
        EnrollmentResource resource = new EnrollmentResource(enrollment);
        
        // Add HATEOAS links
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentById(enrollment.getId())).withSelfRel());
        resource.add(linkTo(methodOn(EnrollmentController.class).getAllEnrollments(0, 10)).withRel("enrollments"));
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentsByPersonId(enrollment.getPersonId(), 0, 10)).withRel("person-enrollments"));
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentsByPilgrimageId(enrollment.getPilgrimageId(), 0, 10)).withRel("pilgrimage-enrollments"));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment found",
                    content = @Content(schema = @Schema(implementation = EnrollmentResource.class))),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentResource> getEnrollmentById(
            @Parameter(description = "Enrollment ID") @PathVariable Long id) {
        EnrollmentDto enrollment = enrollmentService.getEnrollmentById(id);
        EnrollmentResource resource = new EnrollmentResource(enrollment);
        
        // Add HATEOAS links
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentById(id)).withSelfRel());
        resource.add(linkTo(methodOn(EnrollmentController.class).getAllEnrollments(0, 10)).withRel("enrollments"));
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentsByPersonId(enrollment.getPersonId(), 0, 10)).withRel("person-enrollments"));
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentsByPilgrimageId(enrollment.getPilgrimageId(), 0, 10)).withRel("pilgrimage-enrollments"));
        
        return ResponseEntity.ok(resource);
    }
    
    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Retrieve all enrollments with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    public ResponseEntity<PagedResponse<EnrollmentDto>> getAllEnrollments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        PagedResponse<EnrollmentDto> response = enrollmentService.getAllEnrollments(page, size);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/person/{personId}")
    @Operation(summary = "Get enrollments by person ID", description = "Retrieve all enrollments for a specific person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    public ResponseEntity<PagedResponse<EnrollmentDto>> getEnrollmentsByPersonId(
            @Parameter(description = "Person ID") @PathVariable Long personId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        PagedResponse<EnrollmentDto> response = enrollmentService.getEnrollmentsByPersonId(personId, page, size);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/pilgrimage/{pilgrimageId}")
    @Operation(summary = "Get enrollments by pilgrimage ID", description = "Retrieve all enrollments for a specific pilgrimage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    public ResponseEntity<PagedResponse<EnrollmentDto>> getEnrollmentsByPilgrimageId(
            @Parameter(description = "Pilgrimage ID") @PathVariable Long pilgrimageId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        PagedResponse<EnrollmentDto> response = enrollmentService.getEnrollmentsByPilgrimageId(pilgrimageId, page, size);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get enrollments by status", description = "Retrieve all enrollments with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    public ResponseEntity<PagedResponse<EnrollmentDto>> getEnrollmentsByStatus(
            @Parameter(description = "Enrollment status") @PathVariable Enrollment.EnrollmentStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        PagedResponse<EnrollmentDto> response = enrollmentService.getEnrollmentsByStatus(status, page, size);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update enrollment", description = "Update an existing enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment updated successfully",
                    content = @Content(schema = @Schema(implementation = EnrollmentResource.class))),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<EnrollmentResource> updateEnrollment(
            @Parameter(description = "Enrollment ID") @PathVariable Long id,
            @Valid @RequestBody UpdateEnrollmentRequest request) {
        EnrollmentDto enrollment = enrollmentService.updateEnrollment(id, request);
        EnrollmentResource resource = new EnrollmentResource(enrollment);
        
        // Add HATEOAS links
        resource.add(linkTo(methodOn(EnrollmentController.class).getEnrollmentById(id)).withSelfRel());
        resource.add(linkTo(methodOn(EnrollmentController.class).getAllEnrollments(0, 10)).withRel("enrollments"));
        
        return ResponseEntity.ok(resource);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", description = "Delete an enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public ResponseEntity<Void> deleteEnrollment(
            @Parameter(description = "Enrollment ID") @PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/check")
    @Operation(summary = "Check if enrollment exists", description = "Check if a person is enrolled in a specific pilgrimage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> checkEnrollmentExists(
            @Parameter(description = "Person ID") @RequestParam Long personId,
            @Parameter(description = "Pilgrimage ID") @RequestParam Long pilgrimageId) {
        boolean exists = enrollmentService.existsByPersonIdAndPilgrimageId(personId, pilgrimageId);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count")
    @Operation(summary = "Count enrollments by pilgrimage and status", description = "Count enrollments for a pilgrimage with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countByPilgrimageIdAndStatus(
            @Parameter(description = "Pilgrimage ID") @RequestParam Long pilgrimageId,
            @Parameter(description = "Enrollment status") @RequestParam Enrollment.EnrollmentStatus status) {
        long count = enrollmentService.countByPilgrimageIdAndStatus(pilgrimageId, status);
        return ResponseEntity.ok(count);
    }
} 