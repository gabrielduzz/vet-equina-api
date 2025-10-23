package br.com.vetequina.api.mapper;

import org.springframework.stereotype.Component;

import br.com.vetequina.api.dto.horse.*;
import br.com.vetequina.api.entity.Horse;

@Component
public class HorseMapper {

    public Horse toEntity(HorseCreateRequest req) {
        if (req == null)
            return null;
        Horse h = new Horse();
        h.setName(req.name());
        h.setBreed(req.breed());
        h.setBirthdate(req.birthdate());
        h.setPhotoUrl(req.photoUrl());
        return h;
    }

    public void update(Horse target, HorseUpdateRequest req) {
        if (target == null || req == null)
            return;
        if (req.name() != null)
            target.setName(req.name());
        if (req.breed() != null)
            target.setBreed(req.breed());
        if (req.birthdate() != null)
            target.setBirthdate(req.birthdate());
        if (req.photoUrl() != null)
            target.setPhotoUrl(req.photoUrl());
    }

    public HorseResponse toResponse(Horse h) {
        if (h == null)
            return null;
        return new HorseResponse(
                h.getId(),
                h.getName(),
                h.getBreed(),
                h.getBirthdate(),
                h.getPhotoUrl());
    }
}
