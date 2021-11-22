package net.qmate.sender.service.impl;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.controller.event.req.EventReq;
import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.MessageStatus;
import net.qmate.sender.service.EventService;
import net.qmate.sender.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final MessageService messageService;

    @Override
    public void processEvent(EventReq event) {
        MessageEntity messageEntity = new MessageEntity()
                .setStatus(MessageStatus.NEW)
                .setEventType(event.getEventType())
                .setTicketId(event.getTicketId())
                .setCreateDateTime(LocalDateTime.now())
                .setUpdateDateTime(LocalDateTime.now());
        messageService.saveMessage(messageEntity);
    }
}
