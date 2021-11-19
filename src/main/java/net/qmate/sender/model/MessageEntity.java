package net.qmate.sender.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.qmate.sender.model.enums.CpaResponseStatus;
import net.qmate.sender.model.enums.EventType;
import net.qmate.sender.model.enums.MessageStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_seq")
    private Long id;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    @Enumerated(EnumType.STRING)
    private CpaResponseStatus cpaResponseStatus;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private Long ticketId;
    private String phone;
    private String locale;
    private Long ticketTitle;
    private String workplaceTitle;

}
