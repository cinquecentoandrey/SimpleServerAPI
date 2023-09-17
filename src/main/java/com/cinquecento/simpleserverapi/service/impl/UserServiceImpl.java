package com.cinquecento.simpleserverapi.service.impl;

import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;
import com.cinquecento.simpleserverapi.repository.UserRepository;
import com.cinquecento.simpleserverapi.service.UserService;
import com.cinquecento.simpleserverapi.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Long save(User user) {
        user.setStatus(Status.ABSENT);
        return userRepository.save(user).getId();
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
    @Transactional
    public void update(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStatus(User user, Status status) {
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public List<User> findByStatus(Status status, String propertyForSort, String order) {
        if (order.equalsIgnoreCase("asc")) {
            return userRepository.findAllByStatus(status, Sort.by(propertyForSort.toLowerCase()).ascending());
        } else {
            return userRepository.findAllByStatus(status, Sort.by(propertyForSort.toLowerCase()).descending());
        }
    }
}
