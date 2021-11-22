package net.qmate.sender.service;

import net.qmate.sender.controller.event.req.EventReq;

public interface EventService {
    void processEvent(EventReq event);
}
