package br.com.vetequina.api.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.vetequina.api.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    Page<MedicalRecord> findByHorseId(UUID horseId, Pageable pageable);

    // para checar posse: combinar com exists de Horse no service
    Optional<MedicalRecord> findByIdAndHorseId(UUID recordId, UUID horseId);
}
