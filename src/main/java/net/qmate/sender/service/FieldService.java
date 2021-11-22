package net.qmate.sender.service;

import net.qmate.sender.controller.field.req.FieldReq;
import net.qmate.sender.controller.field.req.TicketTitleReq;
import net.qmate.sender.service.exceptions.FailQueueReadException;

public interface FieldService {
    String getFieldValue(FieldReq fieldReq) throws FailQueueReadException;

    Long getTicketTitle(TicketTitleReq req) throws FailQueueReadException;

    String getWorkplaceTitle(TicketTitleReq req) throws FailQueueReadException;

}
