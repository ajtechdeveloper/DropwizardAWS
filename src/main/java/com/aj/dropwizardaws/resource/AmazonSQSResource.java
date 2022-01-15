package com.aj.dropwizardaws.resource;

import com.aj.dropwizardaws.domain.AmazonSQSRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/amazonSQS")
@Produces(MediaType.APPLICATION_JSON)
public class AmazonSQSResource {

    private static AmazonSQS amazonSQS;

    public AmazonSQSResource(AmazonSQS builtSQS) {
        amazonSQS = builtSQS;
    }

    private static final Logger logger = LoggerFactory.getLogger(AmazonSQSResource.class);

    @Path("/createQueue/{sqsQueueName}")
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response createQueue(@PathParam("sqsQueueName") String sqsQueueName) {
        logger.info("SQS Queue Name is: {}", sqsQueueName);
        CreateQueueRequest request = new CreateQueueRequest().withQueueName(sqsQueueName);
        CreateQueueResult result = amazonSQS.createQueue(request);
        Map<String, String> response = new HashMap<>();
        response.put("queueUrl", result.getQueueUrl());
        return Response.ok(response).build();
    }

    @Path("/listQueues")
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listQueues() {
        List<String> queueUrls = amazonSQS.listQueues().getQueueUrls();
        Map<String, List<String>> response = new HashMap<>();
        response.put("sqsQueueURLs", queueUrls);
        return Response.ok(response).build();
    }

    @Path("/sendMessage")
    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(AmazonSQSRequest amazonSQSRequest) {
        logger.info("Request received is: {}", amazonSQSRequest.toString());
        SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
                .withQueueUrl(amazonSQSRequest.getQueueUrl())
                .withMessageBody(amazonSQSRequest.getMessageBody())
                .withDelaySeconds(amazonSQSRequest.getDelaySeconds())
                .withMessageAttributes(amazonSQSRequest.getMessageAttributes());
        amazonSQS.sendMessage(sendMessageStandardQueue);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Message sent");
        return Response.ok(response).build();
    }

    @Path("/receiveMessage")
    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receiveMessage(AmazonSQSRequest amazonSQSRequest) {
        logger.info("Request received is: {}", amazonSQSRequest.toString());
        Map<String, List<Message>> response = new HashMap<>();
        List<Message> messages = amazonSQS.receiveMessage(new ReceiveMessageRequest(amazonSQSRequest.
                getQueueUrl()).withAttributeNames("All").withMessageAttributeNames("All")).getMessages();
        logger.info("Number of messages are: {}", messages.size());
        response.put("message", messages);
        return Response.ok(response).build();
    }
}
