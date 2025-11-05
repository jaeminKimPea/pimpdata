package com.pimp.common.domain.user.presentation;

import com.pimp.common.domain.user.dto.request.UserCreateRequestDto;
import com.pimp.common.domain.user.dto.request.UserUpdateRequestDto;
import com.pimp.common.domain.user.dto.response.UserResponseDto;
import com.pimp.common.domain.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

