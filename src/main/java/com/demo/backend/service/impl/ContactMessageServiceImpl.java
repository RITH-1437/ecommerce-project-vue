package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.ContactMessage;
import com.demo.backend.model.enums.ContactStatus;
import com.demo.backend.repository.ContactMessageRepository;
import com.demo.backend.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository messageRepository;

    @Override
    public ContactMessage create(ContactMessage msg) {
        return messageRepository.save(msg);
    }

    @Override
    public ContactMessage updateStatus(Long id, ContactStatus status) {
        ContactMessage msg = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Message not found"));

        msg.setStatus(status);
        return messageRepository.save(msg);
    }

    @Override
    public List<ContactMessage> findByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }

    @Override
    public List<ContactMessage> findByStatus(ContactStatus status) {
        return messageRepository.findByStatus(status);
    }
}
