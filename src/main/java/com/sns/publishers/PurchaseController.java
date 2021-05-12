package com.sns.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.publishers.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Controller
public class PurchaseController {

    @Autowired
    SnsClient snsClient;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/purchase")
    @ResponseBody
    public String publishSaleMessage(@RequestBody Product product) throws JsonProcessingException {

        PublishRequest request = PublishRequest.builder()
                .message(objectMapper.writeValueAsString(product))
                .topicArn(product.getTopicArn())
                .build();

        PublishResponse result = snsClient.publish(request);
        return  result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode();
    }
}
