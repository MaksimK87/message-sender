package net.qmate.sender.controller.event;

import lombok.RequiredArgsConstructor;
import net.qmate.sender.controller.event.req.EventReq;
import net.qmate.sender.controller.event.resp.BooleanResp;
import net.qmate.sender.service.EventService;
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
