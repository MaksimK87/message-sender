package net.qmate.sender.controller.field.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@AllArgsConstructor
@Getter
@Setter
public class TicketTitleReq {
    private UUID ticketId;
}