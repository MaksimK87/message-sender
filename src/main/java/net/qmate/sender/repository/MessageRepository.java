package net.qmate.sender.repository;

import net.qmate.sender.model.MessageEntity;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByStatusAndCreateDateTimeBetween(MessageStatus status, LocalDateTime from, LocalDateTime currentTime);
}
