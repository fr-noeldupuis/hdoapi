package fr.noeldupuis.hdoapi.pilgrimage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePilgrimageRequest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
} 