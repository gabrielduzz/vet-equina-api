package br.com.vetequina.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.vetequina.api.entity.User;
import br.com.vetequina.api.security.CurrentUser;
import br.com.vetequina.api.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping("/me")
    public ResponseEntity<User> me() {
        UUID uid = currentUser.id();
        return ResponseEntity.ok(userService.getMyProfile(uid));
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMe(@RequestBody Map<String, String> body) {
        UUID uid = currentUser.id();
        String firstName = body.getOrDefault("firstName", null);
        String lastName = body.getOrDefault("lastName", null);
        return ResponseEntity.ok(userService.updateMyProfile(uid, firstName, lastName));
    }

    // @GetMapping("/users")
    // public ResponseEntity<List<User>> search(@RequestParam(required = false, name
    // = "q") String q) {
    // return ResponseEntity.ok(service.searchByName(q));
    // }

    @PostMapping("/users/sync")
    public ResponseEntity<User> syncFromAuth(@RequestBody SyncFromAuthRequest body) {

        return ResponseEntity
                .ok(userService.upsertFromAuth(body.userId(), body.email(), body.firstName(), body.lastName()));
    }

    public record UpdateProfileRequest(String firstName, String lastName) {
    }

    public record SyncFromAuthRequest(UUID userId, String email, String firstName, String lastName) {
    }
}
