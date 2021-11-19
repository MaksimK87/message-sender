package net.qmate.sender.service.impl;

import net.qmate.sender.model.MessageEntity;

import java.util.List;

public interface MessageService {

    String createMessageForSubscriber(MessageEntity messageEntity);

    MessageEntity saveMessage(MessageEntity messageEntity);

    List<MessageEntity> getAllNewMessages();

    MessageEntity setFields(MessageEntity messageEntity);

}
