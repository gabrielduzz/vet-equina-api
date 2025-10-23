package br.com.vetequina.api.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.vetequina.api.entity.Horse;

public interface HorseService {

    Horse createForCurrentUser(Horse toCreate, UUID currentUserId);

    Page<Horse> listMyHorses(UUID currentUserId, Pageable pageable);

    Horse getMyHorse(UUID horseId, UUID currentUserId);

    Horse updateMyHorse(UUID horseId, Horse updates, UUID currentUserId);

    void deleteMyHorse(UUID horseId, UUID currentUserId);
}
