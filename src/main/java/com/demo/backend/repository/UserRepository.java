package com.demo.backend.repository;

import com.demo.backend.model.User;
import com.demo.backend.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    boolean existsByEmail(String email);
    long countByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE %:kw% OR LOWER(u.email) LIKE %:kw%")
    Page<User> search(@Param("kw") String keyword, Pageable pageable);

}
