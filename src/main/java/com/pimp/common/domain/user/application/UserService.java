package com.pimp.common.domain.user.application;

import com.pimp.common.domain.user.domain.model.User;
import com.pimp.common.domain.user.domain.repository.UserRepository;
import com.pimp.common.domain.user.dto.request.UserCreateRequestDto;
import com.pimp.common.domain.user.dto.request.UserUpdateRequestDto;
import com.pimp.common.domain.user.dto.response.UserResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserCreateRequestDto req) {
        User user = User.builder()
                .name(req.getName())
                .userId(req.getUserId())
                .email(req.getEmail())
                .interlockEmail(req.getInterlockEmail())
                .build();

        return toResponse(userRepository.save(user));
    }

    public UserResponseDto updateUser(Long id, UserUpdateRequestDto req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + id));

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setInterlockEmail(req.getInterlockEmail());
        return toResponse(userRepository.save(user));
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .userId(user.getUserId())
                .email(user.getEmail())
                .interlockEmail(user.getInterlockEmail())
                .build();
    }
}

