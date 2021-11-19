package net.qmate.sender.service;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.controller.req.EventReq;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.MessageStatus;
import net.qmate.sender.service.impl.EventService;
import net.qmate.sender.service.impl.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final MessageService messageService;

    @Override
    public void processEvent(EventReq event) {
        MessageEntity messageEntity = new MessageEntity()
                .setEventType(event.getEventType())
                .setCreateDateTime(LocalDateTime.now())
                .setStatus(MessageStatus.NEW)
                .setTicketId(event.getTicketId());
        messageService.saveMessage(messageEntity);
    }
}
