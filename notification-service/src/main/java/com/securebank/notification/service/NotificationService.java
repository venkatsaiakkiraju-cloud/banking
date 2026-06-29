package com.securebank.notification.service;
import com.securebank.notification.dto.NotificationDto;
import com.securebank.notification.model.Notification;
import com.securebank.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j @Service @RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repo;

    @Transactional
    public NotificationDto.NotificationResponse send(NotificationDto.SendRequest req) {
        // Simulated dispatch — in production this would integrate with an email/SMS provider (SES, Twilio, etc.)
        Notification.NotificationStatus status = Notification.NotificationStatus.SENT;
        try {
            log.info("Dispatching {} to {} - subject: {}", req.getType(), req.getRecipient(), req.getSubject());
            // simulate send success
        } catch(Exception e) {
            status = Notification.NotificationStatus.FAILED;
            log.error("Failed to send notification to {}: {}", req.getRecipient(), e.getMessage());
        }
        var n = Notification.builder().recipient(req.getRecipient()).type(req.getType())
            .subject(req.getSubject()).message(req.getMessage()).status(status).build();
        var saved = repo.save(n);
        return toResponse(saved);
    }

    public List<NotificationDto.NotificationResponse> getByRecipient(String recipient) {
        return repo.findByRecipientOrderByCreatedAtDesc(recipient).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private NotificationDto.NotificationResponse toResponse(Notification n) {
        return NotificationDto.NotificationResponse.builder().id(n.getId()).recipient(n.getRecipient()).type(n.getType())
            .subject(n.getSubject()).message(n.getMessage()).status(n.getStatus()).createdAt(n.getCreatedAt()).build();
    }
}
