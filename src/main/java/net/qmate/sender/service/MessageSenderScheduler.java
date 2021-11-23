package net.qmate.sender.service;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.CpaResponseStatus;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.enums.MessageStatus;
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
    @Value("${cpa.request.header.basic.auth}")
    private String basicAuth;
    @Value("${cpa.request.body.source-number}")
    private String providerServiceNumber;
    @Value("${cpa.request.body.bearer-type}")
    private String bearerType;
    @Value("${cpa.request.body.content-type}")
    private String contentType;

    @Scheduled(fixedDelayString = "${scheduler.delay}")
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
            if (messageEntity.getStatus().equals(MessageStatus.SUCCESS_QUEUE_READ)) {
                String messageForSubscriber = messageService.createMessageForSubscriber(messageEntity);
                sendRequest(messageForSubscriber, messageEntity);
            }
        });
    }

    private void sendRequest(String message, MessageEntity messageEntity) {
        HttpHeaders headers = getHeaders();
        Map<String, String> map = new HashMap<>();
        map.put("source", providerServiceNumber);
        map.put("destination", messageEntity.getPhone());
        map.put("bearerType", bearerType);
        map.put("contentType", contentType);
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
            if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.BAD_REQUEST);
            }
            if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.UNAUTHORIZED);
            }
            if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.FORBIDDEN);
            }
            if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.NOT_FOUND);
            }

            if (responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.INTERNAL_ERROR);
            }
            if (responseEntity.getStatusCode() == HttpStatus.BAD_GATEWAY) {
                messageEntity
                        .setCpaResponseStatus(CpaResponseStatus.BAD_GATEWAY);
            }
        }
        messageEntity
                .setStatus(MessageStatus.FAIL_SENT)
                .setUpdateDateTime(LocalDateTime.now());
        messageService.saveMessage(messageEntity);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(basicAuth);
        return headers;
    }
}

