package com.example.camunda8demo.controller;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private final ZeebeClient zeebeClient;

    public ProcessController(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startProcess(@RequestBody Map<String, Object> variables) {
        ProcessInstanceEvent event = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId("order-process")
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        return ResponseEntity.ok(Map.of(
                "processInstanceKey", event.getProcessInstanceKey(),
                "bpmnProcessId", event.getBpmnProcessId(),
                "version", event.getVersion()
        ));
    }

}
