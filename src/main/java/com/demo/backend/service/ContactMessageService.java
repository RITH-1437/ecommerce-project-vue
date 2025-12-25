package com.demo.backend.service;

import com.demo.backend.model.ContactMessage;
import com.demo.backend.model.enums.ContactStatus;

import java.util.List;

public interface ContactMessageService {

    ContactMessage create(ContactMessage msg);

    ContactMessage updateStatus(Long id, ContactStatus status);

    List<ContactMessage> findByUserId(Long userId);

    List<ContactMessage> findByStatus(ContactStatus status);
}
