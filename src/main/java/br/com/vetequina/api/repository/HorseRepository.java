package br.com.vetequina.api.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.vetequina.api.entity.Horse;

public interface HorseRepository extends JpaRepository<Horse, UUID> {

    Page<Horse> findByOwnerId(UUID ownerId, Pageable pageable);

    Optional<Horse> findByIdAndOwnerId(UUID horseId, UUID ownerId);

    boolean existsByIdAndOwnerId(UUID horseId, UUID ownerId);
}
