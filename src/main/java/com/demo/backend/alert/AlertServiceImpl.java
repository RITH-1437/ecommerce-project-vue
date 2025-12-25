package com.demo.backend.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${alert.admin.email:}")
    private String adminEmail;

    @Value("${alert.email.from:no-reply@localhost}")
    private String emailFrom;

    @Value("${alert.telegram.bot-token:}")
    private String telegramBotToken;

    @Value("${alert.telegram.chat-id:}")
    private String telegramChatId;

    @Override
    public Alert createAlert(AlertType type, AlertSeverity severity, String message, Map<String, Object> metadata) {
        try {
            String metaJson = metadata == null ? null : objectMapper.writeValueAsString(metadata);
            Alert a = Alert.builder()
                    .type(type)
                    .severity(severity)
                    .message(message)
                    .metadata(metaJson)
                    .build();

            Alert saved = alertRepository.save(a);

            // Send notifications (best-effort — do not fail alert creation)
            sendEmailNotification(saved);
            sendTelegramNotification(saved);

            return saved;
        } catch (Exception ex) {
            // If serializing or notifications fail, still persist minimal alert
            Alert a = Alert.builder()
                    .type(type)
                    .severity(severity)
                    .message(message + " (notification error: " + ex.getMessage() + ")")
                    .metadata(null)
                    .build();
            return alertRepository.save(a);
        }
    }

    @Override
    public List<Alert> listActiveAlerts() {
        return alertRepository.findByAcknowledgedFalseOrderByCreatedAtDesc();
    }

    @Override
    public Alert acknowledge(Long id, String by) {
        Alert a = alertRepository.findById(id).orElseThrow();
        a.setAcknowledged(true);
        a.setAcknowledgedBy(by);
        a.setAcknowledgedAt(LocalDateTime.now());
        return alertRepository.save(a);
    }

    private void sendEmailNotification(Alert alert) {
        if (adminEmail == null || adminEmail.isBlank()) return;
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(emailFrom);
            msg.setTo(adminEmail);
            msg.setSubject("[ALERT] " + alert.getType() + " / " + alert.getSeverity());
            String body = alert.getMessage()
                    + (alert.getMetadata() != null ? ("\n\nMetadata:\n" + alert.getMetadata()) : "");
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception ignored) { }
    }

    private void sendTelegramNotification(Alert alert) {
        if (telegramBotToken == null || telegramBotToken.isBlank() || telegramChatId == null || telegramChatId.isBlank()) return;
        try {
            String url = String.format("https://api.telegram.org/bot%s/sendMessage", telegramBotToken);
            String text = String.format("*%s* (%s)%n%s",
                    alert.getType(), alert.getSeverity(), alert.getMessage());
            Map<String, String> payload = Map.of(
                    "chat_id", telegramChatId,
                    "text", text,
                    "parse_mode", "Markdown"
            );
            restTemplate.postForObject(url, payload, String.class);
        } catch (Exception ignored) { }
    }
}
