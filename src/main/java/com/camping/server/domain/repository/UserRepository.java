package com.camping.server.domain.repository;

import com.camping.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByOpenId(String openId);
}
