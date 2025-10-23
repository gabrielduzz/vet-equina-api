package br.com.vetequina.api.dto.med;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record MedicalRecordUpdateRequest(
        LocalDate date,
        @Size(max = 4000) String description,
        @Size(max = 2048) String fileUrl) {
}