package com.example.petProject.repository;

import com.example.petProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCaseAndEnabled(String username, boolean enabled);
}
