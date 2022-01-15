package com.aj.dropwizardaws.domain;

import com.amazonaws.services.sqs.model.MessageAttributeValue;

import java.util.Map;

public class AmazonSQSRequest {

    private String queueUrl;
    private String messageBody;
    private int delaySeconds;
    private Map<String, MessageAttributeValue> messageAttributes;

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public Map<String, MessageAttributeValue> getMessageAttributes() {
        return messageAttributes;
    }

    public void setMessageAttributes(Map<String, MessageAttributeValue> messageAttributes) {
        this.messageAttributes = messageAttributes;
    }

    @Override
    public String toString() {
        return "AmazonSQSRequest{" +
                "queueUrl='" + queueUrl + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", delaySeconds=" + delaySeconds +
                ", messageAttributes=" + messageAttributes +
                '}';
    }
}
