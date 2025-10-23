package br.com.vetequina.api.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "horses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // dono (tutor) — referencia sua tabela users
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @NotBlank
    @Size(min = 1, max = 120)
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Size(max = 120)
    @Column(name = "breed", length = 120)
    private String breed;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    // URL pública ou caminho no bucket (defina um padrão: ex. storage path)
    @Size(max = 2048)
    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
