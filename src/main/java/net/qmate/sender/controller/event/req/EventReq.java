package net.qmate.sender.controller.event.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.qmate.sender.model.enums.EventType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class EventReq {
    private UUID ticketId;
    private EventType eventType;
}
