package br.com.vetequina.api.dto.horse;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record HorseCreateRequest(
                @NotBlank @Size(max = 120) String name,
                @Size(max = 120) String breed,
                @NotNull LocalDate birthdate,
                @Size(max = 2048) String photoUrl) {
}