package net.qmate.sender.service.impl;

import net.qmate.sender.controller.req.EventReq;

import java.util.HashMap;

public interface EventService {
    void processEvent(EventReq event);
}
