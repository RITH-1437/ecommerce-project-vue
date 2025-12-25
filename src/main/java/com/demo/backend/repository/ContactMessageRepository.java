package com.demo.backend.repository;

import com.demo.backend.model.ContactMessage;
import com.demo.backend.model.enums.ContactStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByUserId(Long userId);
    List<ContactMessage> findByStatus(ContactStatus status);
}
