package br.com.vetequina.api.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.vetequina.api.entity.MedicalRecord;

public interface MedicalRecordService {

    MedicalRecord createForMyHorse(UUID horseId, MedicalRecord toCreate, UUID currentUserId);

    Page<MedicalRecord> listForMyHorse(UUID horseId, UUID currentUserId, Pageable pageable);

    MedicalRecord getMyRecord(UUID horseId, UUID recordId, UUID currentUserId);

    MedicalRecord updateMyRecord(UUID horseId, UUID recordId, MedicalRecord updates, UUID currentUserId);

    void deleteMyRecord(UUID horseId, UUID recordId, UUID currentUserId);
}
