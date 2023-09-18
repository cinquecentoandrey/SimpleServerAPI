package com.cinquecento.simpleserverapi.service;

import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface UserService {

    Long save(User user);

    User findById(Long id);

    User findByUsername(String username);

    void update(Long id, User user);

    void delete(Long id);

    void updateStatus(User user, Status status);

    List<User> findByStatus(Status status, String propertyForSort, String order);

    List<User> findByStatus(Status status, LocalDateTime timestamp);
}
