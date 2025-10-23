package br.com.vetequina.api.dto.med;

import java.time.LocalDate;
import java.util.UUID;

public record MedicalRecordResponse(
        UUID id,
        UUID horseId,
        LocalDate date,
        String description,
        String fileUrl) {
}