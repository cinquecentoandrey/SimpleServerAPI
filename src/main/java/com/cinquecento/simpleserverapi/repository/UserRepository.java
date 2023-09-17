package com.cinquecento.simpleserverapi.repository;

import com.cinquecento.simpleserverapi.model.Status;
import com.cinquecento.simpleserverapi.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    List<User> findAllByStatus(Status status, Sort sort);

}
