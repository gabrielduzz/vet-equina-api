package br.com.vetequina.api.dto.horse;

import java.time.LocalDate;
import java.util.UUID;

public record HorseResponse(
                UUID id,
                String name,
                String breed,
                LocalDate birthdate,
                String photoUrl) {
}