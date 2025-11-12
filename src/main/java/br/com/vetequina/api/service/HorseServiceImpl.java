package br.com.vetequina.api.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.entity.User;
import br.com.vetequina.api.repository.HorseRepository;
import br.com.vetequina.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HorseServiceImpl implements HorseService {

    private final HorseRepository horses;
    private final UserRepository users;

    @Override
    public Horse createForCurrentUser(Horse toCreate, UUID currentUserId) {
        User owner = users.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        toCreate.setId(null);
        toCreate.setOwner(owner);
        return horses.save(toCreate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Horse> listMyHorses(UUID currentUserId, Pageable pageable) {
        return horses.findByOwnerId(currentUserId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Horse getMyHorse(UUID horseId, UUID currentUserId) {
        return horses.findByIdAndOwnerId(horseId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Cavalo não encontrado"));
    }

    @Override
    public Horse updateMyHorse(UUID horseId, Horse updates, UUID currentUserId) {
        Horse entity = getMyHorse(horseId, currentUserId);
        if (updates.getName() != null)
            entity.setName(updates.getName());
        if (updates.getBreed() != null)
            entity.setBreed(updates.getBreed());
        if (updates.getBirthdate() != null)
            entity.setBirthdate(updates.getBirthdate());
        if (updates.getPhotoUrl() != null)
            entity.setPhotoUrl(updates.getPhotoUrl());
        return horses.save(entity);
    }

    @Override
    public void deleteMyHorse(UUID horseId, UUID currentUserId) {
        Horse entity = getMyHorse(horseId, currentUserId);
        horses.delete(entity);
    }
}
