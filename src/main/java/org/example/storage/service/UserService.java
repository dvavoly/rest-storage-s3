package org.example.storage.service;

import org.example.storage.exception.UserNotFoundByIdException;
import org.example.storage.model.User;
import org.example.storage.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User updateUser) {
        var existedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
        return userRepository.save(copyFields(updateUser, existedUser));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private User copyFields(User fromUser, User toUser) {
        if (fromUser.getFirstName() != null) {
            toUser.setFirstName(fromUser.getFirstName());
        }
        if (fromUser.getLastName() != null) {
            toUser.setLastName(fromUser.getLastName());
        }
        if (fromUser.getEmail() != null) {
            toUser.setEmail(fromUser.getEmail());
        }
        if (fromUser.getPassword() != null) {
            toUser.setPassword(passwordEncoder.encode(fromUser.getPassword()));
        }
        if (fromUser.getRole() != null) {
            toUser.setRole(fromUser.getRole());
        }
        if (fromUser.getStatus() != null) {
            toUser.setStatus(fromUser.getStatus());
        }
        return toUser;
    }
}
