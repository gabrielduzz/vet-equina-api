package br.com.vetequina.api.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.vetequina.api.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    Page<MedicalRecord> findByHorseId(UUID horseId, Pageable pageable);

    Optional<MedicalRecord> findByIdAndHorseId(UUID recordId, UUID horseId);
}
