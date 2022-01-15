package com.aj.dropwizardaws.domain;

public class AmazonSNSRequest {

    private String topicArn;
    private String message;
    private String subject;
    private String queueUrl;

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    @Override
    public String toString() {
        return "AmazonSNSRequest{" +
                "topicArn='" + topicArn + '\'' +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", queueUrl='" + queueUrl + '\'' +
                '}';
    }
}
