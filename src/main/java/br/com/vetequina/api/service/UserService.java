package br.com.vetequina.api.service;

import java.util.List;
import java.util.UUID;
import br.com.vetequina.api.entity.User;

public interface UserService {
    User upsertFromAuth(UUID userId, String email, String firstName, String lastName);

    User getMyProfile(UUID currentUserId);

    User updateMyProfile(UUID currentUserId, String firstName, String lastName);
}
