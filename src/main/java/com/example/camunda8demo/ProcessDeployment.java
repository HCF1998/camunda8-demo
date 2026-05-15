package com.example.camunda8demo;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ProcessDeployment implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProcessDeployment.class);

    private final ZeebeClient zeebeClient;

    public ProcessDeployment(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    public void run(String... args) {
        try {
            ClassPathResource bpmnResource = new ClassPathResource("processes/order-process.bpmn");
            DeploymentEvent deployment = zeebeClient
                    .newDeployResourceCommand()
                    .addResourceStream(bpmnResource.getInputStream(), "order-process.bpmn")
                    .send()
                    .join();

            log.info("Deployed process: {}", deployment.getProcesses().get(0).getBpmnProcessId());
        } catch (Exception e) {
            log.error("Failed to deploy process", e);
        }
    }

}
