package br.com.vetequina.api.controller;

import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.mapper.HorseMapper;
import br.com.vetequina.api.security.CurrentUser;
import br.com.vetequina.api.dto.horse.*;
import br.com.vetequina.api.service.HorseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/horses")
@RequiredArgsConstructor
public class HorseController {

    private final HorseService service;
    private final HorseMapper mapper;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<HorseResponse> create(@Valid @RequestBody HorseCreateRequest body) {
        Horse entity = mapper.toEntity(body);
        Horse saved = service.createForCurrentUser(entity, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<Page<HorseResponse>> list(Pageable pageable) {
        Page<Horse> page = service.listMyHorses(currentUser.id(), pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @GetMapping("/{horseId}")
    public ResponseEntity<HorseResponse> get(@PathVariable UUID horseId) {
        Horse h = service.getMyHorse(horseId, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(h));
    }

    @PutMapping("/{horseId}")
    public ResponseEntity<HorseResponse> update(@PathVariable UUID horseId,
            @Valid @RequestBody HorseUpdateRequest body) {
        Horse entity = service.getMyHorse(horseId, currentUser.id());
        mapper.update(entity, body);
        Horse saved = service.updateMyHorse(horseId, entity, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @DeleteMapping("/{horseId}")
    public ResponseEntity<Void> delete(@PathVariable UUID horseId) {
        service.deleteMyHorse(horseId, currentUser.id());
        return ResponseEntity.noContent().build();
    }
}
