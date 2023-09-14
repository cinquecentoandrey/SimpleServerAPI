package com.cinquecento.simpleserverapi.service.impl;

import com.cinquecento.simpleserverapi.model.User;
import com.cinquecento.simpleserverapi.repository.UserRepository;
import com.cinquecento.simpleserverapi.service.UserService;
import com.cinquecento.simpleserverapi.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id=" + id + " not found."));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username=" + username + " not found."));
    }

    @Override
    public void update(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
