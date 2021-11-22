package net.qmate.sender.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.qmate.sender.controller.field.req.FieldReq;
import net.qmate.sender.controller.field.req.TicketTitleReq;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.enums.MessageStatus;
import net.qmate.sender.repository.MessageRepository;
import net.qmate.sender.service.FieldService;
import net.qmate.sender.service.MessageService;
import net.qmate.sender.service.exceptions.FailQueueReadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String LOCALE = "locale";
    private static final long CREATION_PERIOD_IN_SECONDS = 30;
    private static final String ENTITY_TYPE = "TICKET";

    @Value("${ru.event.registered}")
    private String registeredRu;
    @Value("${uk.event.registered}")
    private String registeredUk;
    @Value("${en.event.registered}")
    private String registeredEn;
    @Value("${ru.event.called}")
    private String calledRu;
    @Value("${uk.event.called}")
    private String calledUk;
    @Value("${en.event.called}")
    private String calledEn;

    private final MessageRepository messageRepository;
    private final FieldService fieldService;

    @Override
    public String createMessageForSubscriber(MessageEntity messageEntity) {
        String message = null;
        if (messageEntity.getEventType().equals(EventType.REGISTERED)) {
            if (messageEntity.getLocale() == null) {
                return createRegisteredMessage(messageEntity, registeredEn);
            }
            switch (messageEntity.getLocale()) {
                case "EN":
                    message = createRegisteredMessage(messageEntity, registeredEn);
                    break;
                case "RU":
                    message = createRegisteredMessage(messageEntity, registeredRu);
                    break;
                case "UK":
                    message = createRegisteredMessage(messageEntity, registeredUk);
                    break;
            }

        } else {
            if (messageEntity.getLocale() == null) {
                return createCalledMessage(messageEntity, calledEn);
            }
            switch (messageEntity.getLocale()) {
                case "EN":
                    message = createCalledMessage(messageEntity, calledEn);
                    break;
                case "RU":
                    message = createCalledMessage(messageEntity, calledRu);
                    break;
                case "UK":
                    message = createCalledMessage(messageEntity, calledUk);
                    break;
            }
        }
        return message;
    }

    @Override
    public MessageEntity saveMessage(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }

    @Override
    public List<MessageEntity> getAllNewMessages() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime from = currentTime.minusSeconds(CREATION_PERIOD_IN_SECONDS);
        return messageRepository.findAllByStatusAndCreateDateTimeBetween(MessageStatus.NEW, from, currentTime);
    }

    @Override
    public MessageEntity setFields(MessageEntity messageEntity) {
        String workplaceTitle = null;
        String phoneNumber;
        String locale ;
        Long ticketTitle;
        try {
            phoneNumber = fieldService.getFieldValue(new FieldReq(messageEntity.getTicketId(), ENTITY_TYPE, PHONE_NUMBER));
            locale = fieldService.getFieldValue(new FieldReq(messageEntity.getTicketId(), ENTITY_TYPE, LOCALE));
            ticketTitle = fieldService.getTicketTitle(new TicketTitleReq(messageEntity.getTicketId()));
            if (messageEntity.getEventType().equals(EventType.CALLED)) {
                workplaceTitle = fieldService.getWorkplaceTitle(new TicketTitleReq(messageEntity.getTicketId()));
            }
        } catch (FailQueueReadException e) {
            messageEntity
                    .setStatus(MessageStatus.FAIL_QUEUE_READ)
                    .setUpdateDateTime(LocalDateTime.now());
            messageRepository.save(messageEntity);
            return messageEntity;
        }
        messageEntity.setPhone(phoneNumber)
                .setLocale(locale)
                .setTicketTitle(ticketTitle)
                .setWorkplaceTitle(workplaceTitle)
                .setStatus(MessageStatus.SUCCESS_QUEUE_READ)
                .setUpdateDateTime(LocalDateTime.now());
        messageRepository.save(messageEntity);
        return messageEntity;
    }

    private String createRegisteredMessage(MessageEntity messageEntity, String locale) {
        return String.format("id: %s, status: %s",
                messageEntity.getTicketTitle(), locale);
    }

    private String createCalledMessage(MessageEntity messageEntity, String locale) {
        return String.format("id: %s, status: %s, workPlace: %s",
                messageEntity.getTicketTitle(), locale, messageEntity.getWorkplaceTitle());
    }

}
