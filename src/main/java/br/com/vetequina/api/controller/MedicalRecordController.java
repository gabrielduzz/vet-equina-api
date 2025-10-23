package br.com.vetequina.api.controller;

import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.vetequina.api.dto.med.*;
import br.com.vetequina.api.entity.MedicalRecord;
import br.com.vetequina.api.mapper.MedicalRecordMapper;
import br.com.vetequina.api.security.CurrentUser;
import br.com.vetequina.api.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/horses/{horseId}/records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService service;
    private final MedicalRecordMapper mapper;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<MedicalRecordResponse> create(@PathVariable UUID horseId,
            @Valid @RequestBody MedicalRecordCreateRequest body) {
        MedicalRecord entity = mapper.toEntity(body);
        MedicalRecord saved = service.createForMyHorse(horseId, entity, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<Page<MedicalRecordResponse>> list(@PathVariable UUID horseId, Pageable pageable) {
        Page<MedicalRecord> page = service.listForMyHorse(horseId, currentUser.id(), pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponse> get(@PathVariable UUID horseId, @PathVariable UUID recordId) {
        MedicalRecord rec = service.getMyRecord(horseId, recordId, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(rec));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponse> update(@PathVariable UUID horseId, @PathVariable UUID recordId,
            @Valid @RequestBody MedicalRecordUpdateRequest body) {
        MedicalRecord rec = service.getMyRecord(horseId, recordId, currentUser.id());
        mapper.update(rec, body);
        MedicalRecord saved = service.updateMyRecord(horseId, recordId, rec, currentUser.id());
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> delete(@PathVariable UUID horseId, @PathVariable UUID recordId) {
        service.deleteMyRecord(horseId, recordId, currentUser.id());
        return ResponseEntity.noContent().build();
    }
}
