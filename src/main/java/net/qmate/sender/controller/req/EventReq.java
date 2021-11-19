package net.qmate.sender.controller.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.qmate.sender.model.enums.EventType;

@Getter
@Setter
@AllArgsConstructor
public class EventReq {
    private Long ticketId;
    private EventType eventType;
}
