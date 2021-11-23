package net.qmate.sender.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qmate.sender.controller.field.req.FieldReq;
import net.qmate.sender.controller.field.req.TicketTitleReq;
import net.qmate.sender.controller.field.resp.FieldResp;
import net.qmate.sender.controller.field.resp.TicketTitleResp;
import net.qmate.sender.controller.field.resp.WorkplaceTitleResp;
import net.qmate.sender.service.FieldService;
import net.qmate.sender.service.exceptions.FailQueueReadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class FieldServiceImpl implements FieldService {

    @Value("${qmate.host}")
    private String qmateHost;
    @Value("${qmate.port}")
    private String qmatePort;
    @Value("${qmate.prefix}")
    private String prefix;

    private final RestTemplate restTemplate;

    @Override
    public String getFieldValue(FieldReq fieldReq) throws FailQueueReadException {
        FieldResp fieldResp = null;
        String url = qmateHost + qmatePort + prefix + "/field";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<FieldReq> fieldReqHttpEntity = new HttpEntity(fieldReq, headers);
        try {
            ResponseEntity<FieldResp> responseEntity = restTemplate.postForEntity(url, fieldReqHttpEntity, FieldResp.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                fieldResp = responseEntity.getBody();
            }
        } catch (HttpStatusCodeException e) {
            log.error("Getting field value from qmate is failed: {} with http status code: {}", fieldReq, e.getStatusCode());
            throw new FailQueueReadException("Getting field value is failed!");
        }
        return fieldResp != null ? fieldResp.getValue() : null;
    }

    @Override
    public Long getTicketTitle(TicketTitleReq req) throws FailQueueReadException {
        TicketTitleResp ticketTitleResp = null;
        String url = qmateHost + qmatePort + prefix + "/ticket-title";
        HttpHeaders headers = getHeaders();
        HttpEntity<TicketTitleReq> titleReqHttpEntity = new HttpEntity(req, headers);
        try {
            ResponseEntity<TicketTitleResp> responseEntity = restTemplate.postForEntity(url, titleReqHttpEntity, TicketTitleResp.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ticketTitleResp = responseEntity.getBody();
            }
        } catch (HttpStatusCodeException e) {
            log.error("Getting ticket title from qmate is failed: {} with http status code: {}", req, e.getStatusCode());
            throw new FailQueueReadException("Getting field value is failed!");
        }
        return ticketTitleResp != null ? ticketTitleResp.getKey() : null;
    }

    @Override
    public String getWorkplaceTitle(TicketTitleReq req) throws FailQueueReadException {
        WorkplaceTitleResp workplaceTitleResp = null;
        String url = qmateHost + qmatePort + prefix + "/workplace-title";
        HttpHeaders headers = getHeaders();
        HttpEntity<TicketTitleReq> titleReqHttpEntity = new HttpEntity(req, headers);
        try {
            ResponseEntity<WorkplaceTitleResp> responseEntity = restTemplate.postForEntity(url, titleReqHttpEntity, WorkplaceTitleResp.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                workplaceTitleResp = responseEntity.getBody();
            }
        } catch (HttpStatusCodeException e) {
            log.error("Getting workplace title from qmate is failed: {} with http status code: {}", req, e.getStatusCode());
            throw new FailQueueReadException("Getting workplace title is failed!");
        }
        return workplaceTitleResp != null ? workplaceTitleResp.getWorkplaceTitle() : null;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
