package net.qmate.sender.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.enums.MessageStatus;
import net.qmate.sender.repository.MessageRepository;
import net.qmate.sender.service.impl.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Setter
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String LOCALE = "locale";
    private static final long CREATION_PERIOD_IN_SECONDS = 30;

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

    @Override
    public String createMessageForSubscriber(MessageEntity messageEntity) {
        String message = null;
        if (messageEntity.getEventType().equals(EventType.REGISTERED)) {
            switch (messageEntity.getLocale()) {
                case "EN":
                    message = createRegisteredMessage(messageEntity,registeredEn);
                    break;
                case "RU":
                    message = createRegisteredMessage(messageEntity,registeredRu);
                    break;
                case "UK":
                    message = createRegisteredMessage(messageEntity,registeredUk);
                    break;
            }

        } else {
            switch (messageEntity.getLocale()) {
                case "EN":
                    message = createCalledMessage(messageEntity,registeredEn);
                    break;
                case "RU":
                    message = createCalledMessage(messageEntity,registeredRu);
                    break;
                case "UK":
                    message = createCalledMessage(messageEntity,registeredUk);
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
        return messageRepository.findByStatusAndTime(MessageStatus.NEW.name(), from, currentTime);
    }

    @Override
    public MessageEntity setFields(MessageEntity messageEntity) {
        String workplaceTitle = null;
        String phoneNumber = messageRepository.getValue(PHONE_NUMBER, messageEntity.getTicketId());
        String locale = messageRepository.getValue(LOCALE, messageEntity.getTicketId());
        Long ticketTitle = messageRepository.getTicketTitle(messageEntity.getTicketId());
        if (messageEntity.getEventType().equals(EventType.CALLED)) {
            workplaceTitle = messageRepository.getWorkPlaceTitle(messageEntity.getTicketId());
        }
        messageEntity.setPhone(phoneNumber)
                .setLocale(locale)
                .setTicketTitle(ticketTitle)
                .setWorkplaceTitle(workplaceTitle);

        return messageEntity;
    }

    private String createRegisteredMessage(MessageEntity messageEntity, String locale) {
        return String.format("Oliphaunt id: %s, status: %s",
                messageEntity.getTicketId(), locale);
    }

    private String createCalledMessage(MessageEntity messageEntity, String locale) {
        return String.format("Oliphaunt id: %s, status: %s, workPlace: %s",
                messageEntity.getTicketId(), locale, messageEntity.getWorkplaceTitle());
    }


}
