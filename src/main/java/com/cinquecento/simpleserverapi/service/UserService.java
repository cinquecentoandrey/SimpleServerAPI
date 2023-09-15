package com.cinquecento.simpleserverapi.service;

import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;

import java.sql.SQLException;

public interface UserService {

    Long save(User user);

    User findById(Long id);

    User findByUsername(String username);

    void update(Long id, User user);

    void delete(Long id);

    void updateStatus(User user, Status status);
}
