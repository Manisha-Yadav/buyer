package com.sns.subscribers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Controller
public class SubscribersController {

    @Autowired
    SnsClient snsClient;

    @GetMapping("/subscribe")
    @ResponseBody
    public String subscribe(@RequestParam String topicArn , @RequestParam String endpoint,
                            @RequestParam String protocol) {
        SubscribeRequest request = SubscribeRequest.builder()
                .protocol(protocol)
                .endpoint(endpoint)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();
        SubscribeResponse result = snsClient.subscribe(request);

        return "Subscription ARN: " + result.subscriptionArn() + "\n\n Status was " + result.sdkHttpResponse().statusCode();
    }
}
