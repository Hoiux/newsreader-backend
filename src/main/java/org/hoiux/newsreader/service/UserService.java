package org.hoiux.newsreader.service;

import java.util.Optional;

import org.hoiux.newsreader.entity.User;
import org.hoiux.newsreader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    // --------------------------------------------------------------------------------------------
    public User createUser(User user) {

        // Fail if user already exists.
        @SuppressWarnings("null")
        Optional<User> existingUser = userRepository.findById(user.getUsername());

        // Don't overwrite existing user.
        if (existingUser.isPresent()) {
            return null;
        }

        return userRepository.save(user);
    }

    // --------------------------------------------------------------------------------------------
    @SuppressWarnings("null")
    public String deleteUser(String username) {

        // Fail if user doesn't exist.
        Optional<User> existingUser = userRepository.findById(username);

        if (!existingUser.isPresent()) {
            return null;
        }

        userRepository.deleteById(username);

        return username;
    }
    // --------------------------------------------------------------------------------------------
    public User getUser(String username) {

        // Fail if user already exists.
        @SuppressWarnings("null")
        Optional<User> existingUser = userRepository.findById(username);

        if (!existingUser.isPresent()) {
            return null;
        }

        return existingUser.get();
    }
}