package net.qmate.sender.controller;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.controller.req.EventReq;
import net.qmate.sender.controller.resp.BooleanResp;
import net.qmate.sender.service.impl.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<BooleanResp> createEvent(@RequestBody EventReq event) {
        eventService.processEvent(event);
        return new ResponseEntity(new BooleanResp(true), HttpStatus.OK);
    }
}
