package com.example.camunda8demo.worker;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderProcessingWorker {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingWorker.class);

    @JobWorker(type = "validate-order", autoComplete = true)
    public Map<String, Object> validateOrder(@Variable String orderId, @Variable String customerName) {
        log.info("Validating order: {} for customer: {}", orderId, customerName);
        return Map.of("orderValidated", true, "validationMessage", "Order validated successfully");
    }

    @JobWorker(type = "process-payment", autoComplete = true)
    public Map<String, Object> processPayment(@Variable String orderId, @Variable Double amount) {
        log.info("Processing payment for order: {} amount: {}", orderId, amount);
        return Map.of("paymentProcessed", true, "transactionId", "TXN-" + System.currentTimeMillis());
    }

    @JobWorker(type = "ship-order", autoComplete = true)
    public Map<String, Object> shipOrder(@Variable String orderId, @Variable String customerName) {
        log.info("Shipping order: {} to customer: {}", orderId, customerName);
        return Map.of("shipped", true, "trackingNumber", "TRACK-" + System.currentTimeMillis());
    }

}
