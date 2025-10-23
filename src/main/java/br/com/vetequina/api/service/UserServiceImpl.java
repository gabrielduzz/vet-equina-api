package br.com.vetequina.api.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.vetequina.api.entity.User;
import br.com.vetequina.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public User upsertFromAuth(UUID userId, String email, String firstName, String lastName) {
        return repo.findById(userId).map(existing -> {
            if (email != null && !email.isBlank())
                existing.setEmail(email);
            if (firstName != null && !firstName.isBlank())
                existing.setFirstName(firstName);
            if (lastName != null && !lastName.isBlank())
                existing.setLastName(lastName);
            return repo.save(existing);
        }).orElseGet(() -> {
            User u = User.builder()
                    .id(userId)
                    .email(email)
                    .firstName(firstName != null && !firstName.isBlank() ? firstName : " ")
                    .lastName(lastName != null && !lastName.isBlank() ? lastName : " ")
                    .build();
            return repo.save(u);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public User getMyProfile(UUID currentUserId) {
        return repo.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado"));
    }

    @Override
    public User updateMyProfile(UUID currentUserId, String firstName, String lastName) {
        User me = getMyProfile(currentUserId);
        if (firstName != null && !firstName.isBlank())
            me.setFirstName(firstName);
        if (lastName != null && !lastName.isBlank())
            me.setLastName(lastName);
        return repo.save(me);
    }
}
