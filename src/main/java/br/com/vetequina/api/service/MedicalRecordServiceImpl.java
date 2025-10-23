package br.com.vetequina.api.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.entity.MedicalRecord;
import br.com.vetequina.api.repository.HorseRepository;
import br.com.vetequina.api.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository records;
    private final HorseRepository horses;

    private Horse assertMyHorse(UUID horseId, UUID currentUserId) {
        return horses.findByIdAndOwnerId(horseId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Cavalo não encontrado ou sem permissão"));
    }

    @Override
    public MedicalRecord createForMyHorse(UUID horseId, MedicalRecord toCreate, UUID currentUserId) {
        Horse horse = assertMyHorse(horseId, currentUserId);
        toCreate.setId(null);
        toCreate.setHorse(horse);
        return records.save(toCreate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecord> listForMyHorse(UUID horseId, UUID currentUserId, Pageable pageable) {
        assertMyHorse(horseId, currentUserId);
        return records.findByHorseId(horseId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord getMyRecord(UUID horseId, UUID recordId, UUID currentUserId) {
        assertMyHorse(horseId, currentUserId);
        return records.findByIdAndHorseId(recordId, horseId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado"));
    }

    @Override
    public MedicalRecord updateMyRecord(UUID horseId, UUID recordId, MedicalRecord updates, UUID currentUserId) {
        MedicalRecord entity = getMyRecord(horseId, recordId, currentUserId);
        if (updates.getDate() != null)
            entity.setDate(updates.getDate());
        if (updates.getDescription() != null)
            entity.setDescription(updates.getDescription());
        if (updates.getFileUrl() != null)
            entity.setFileUrl(updates.getFileUrl());
        return records.save(entity);
    }

    @Override
    public void deleteMyRecord(UUID horseId, UUID recordId, UUID currentUserId) {
        MedicalRecord entity = getMyRecord(horseId, recordId, currentUserId);
        records.delete(entity);
    }
}
