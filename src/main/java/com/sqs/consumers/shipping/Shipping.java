package com.sqs.consumers.shipping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqs.consumers.shipping.pojo.MessageFromQueue;
import com.sns.publishers.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Shipping {

    @Value("${shipping.queue.url}")
    private String shippingOrderQueueUrl;

    @Value("${shipping.queue.max.messages}")
    private int shippingOrderQueueMaxMessagesRead;

    @Value("${shipping.queue.waittime.seconds}")
    private int shippingQueueWaitTimeSeconds;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private ObjectMapper objectMapper;

    private enum RegionShippingTimeMatrix {
        US_EAST_1 (1),
        US_EAST_2 (2),
        US_WEST_1 (3),
        US_WEST_2 (4)
        ;
        int days;

        private RegionShippingTimeMatrix (int days) {
            this.days = days;
        }

    }

    public void processOrder() {
        try {
            ReceiveMessageResponse receiveMessageResponse =
                    sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                            .queueUrl(shippingOrderQueueUrl)
                            .maxNumberOfMessages(shippingOrderQueueMaxMessagesRead)
                            .waitTimeSeconds(shippingQueueWaitTimeSeconds)
                            .build());

            List<Message> messages = receiveMessageResponse.messages();
            System.out.format("%d orders received for shipping at %s %n",
                    messages.size(), LocalDateTime.now());

            for (Message message : messages) {
                String msgBody = message.body();
                MessageFromQueue messageFromQueue = null;
                Product product = null;
                try {
                    messageFromQueue = objectMapper.readValue(msgBody, MessageFromQueue.class);
                    product = objectMapper.readValue(messageFromQueue.getMessage(), Product.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                System.out.format("%s will be delivered to %s in next %d days %n",
                        product.getProductName(), product.getBuyerName(),
                        RegionShippingTimeMatrix.valueOf(product.getShippingRegion()).days);

                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(shippingOrderQueueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build());

                System.out.format("Product %s processed and deleted at %s %n",
                        product.getProductId(), LocalDateTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

