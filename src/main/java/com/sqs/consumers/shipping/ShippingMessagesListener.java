package com.sqs.consumers.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ShippingMessagesListener {

    private ScheduledExecutorService executorService;

    @Autowired
    public ShippingMessagesListener(Shipping shipping) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> shipping.processOrder(),
                5, 10, TimeUnit.SECONDS);
    }
}
