package net.qmate.sender.service;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.model.enums.CpaResponseStatus;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.MessageStatus;
import net.qmate.sender.service.impl.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Component
public class MessageSenderScheduler {

    private final MessageService messageService;
    private final RestTemplate restTemplate;
    @Value("${cpa.host}")
    private String spaHost;

    @Scheduled(fixedDelay = 1000)
    void processMessage() {
        List<MessageEntity> newMessages = messageService.getAllNewMessages();
        List<MessageEntity> registered = new ArrayList<>();
        List<MessageEntity> called = new ArrayList<>();
        newMessages.forEach(messageEntity -> {
                    if (messageEntity.getEventType().equals(EventType.REGISTERED)) {
                        registered.add(messageEntity);
                    } else called.add(messageEntity);
                }
        );
        sendMessage(registered);
        sendMessage(called);
    }

    private void sendMessage(List<MessageEntity> messages) {
        messages.forEach(messageEntity -> {
            messageEntity = messageService.setFields(messageEntity);
            String messageForSubscriber = messageService.createMessageForSubscriber(messageEntity);
            sendRequest(messageForSubscriber, messageEntity);
        });
    }

    private void sendRequest(String message, MessageEntity messageEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("dm9yZGVsOnZvcmRlbA==");
        Map<String, String> map = new HashMap<>();
        map.put("source", "101999");
        map.put("destination", messageEntity.getPhone());
        map.put("bearerType", "sms");
        map.put("contentType", "text/plain");
        map.put("content", message);
        HttpEntity<Map<String, Object>> entity = new HttpEntity(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(spaHost, entity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            messageEntity
                    .setStatus(MessageStatus.SUCCESS_SENT)
                    .setCpaResponseStatus(CpaResponseStatus.ACCEPTED)
                    .setUpdateDateTime(LocalDateTime.now());
            messageService.saveMessage(messageEntity);
        } else {
            if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.BAD_REQUEST);
        }
        messageEntity
                .setStatus(MessageStatus.FAIL_SENT)
                .setUpdateDateTime(LocalDateTime.now());
        messageService.saveMessage(messageEntity);
    }
}

