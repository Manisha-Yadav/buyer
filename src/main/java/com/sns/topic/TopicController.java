package com.sns.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

@Controller
public class TopicController {

    @Autowired
    SnsClient snsClient;

    @GetMapping("/createTopic")
    @ResponseBody
    public String createTopic(@RequestParam String topicName){
        System.out.println("Inside createTopic "+ topicName);

        CreateTopicRequest request = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        CreateTopicResponse response = snsClient.createTopic(request);
        return response.topicArn();
    }
}
