package net.qmate.sender.service;

import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.EventType;

import java.util.List;

public interface MessageService {

    String createMessageForSubscriber(MessageEntity messageEntity);

    MessageEntity saveMessage(MessageEntity messageEntity);

    List<MessageEntity> getAllNewMessages();

    MessageEntity setFields(MessageEntity messageEntity);

}
