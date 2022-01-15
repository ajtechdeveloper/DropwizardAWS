package com.aj.dropwizardaws.resource;

import com.aj.dropwizardaws.domain.AmazonSNSRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/amazonSNS")
@Produces(MediaType.APPLICATION_JSON)
public class AmazonSNSResource {

    private static AmazonSNS amazonSNS;
    private static AmazonSQS amazonSQS;

    public AmazonSNSResource(AmazonSNS builtSNS, AmazonSQS builtSQS) {
        amazonSNS = builtSNS;
        amazonSQS = builtSQS;
    }

    private static final Logger logger = LoggerFactory.getLogger(AmazonSNSResource.class);

    @Path("/createTopic/{snsTopicName}")
    @Timed
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTopic(@PathParam("snsTopicName") String snsTopicName) {
        logger.info("SNS Topic Name is: {}", snsTopicName);
        CreateTopicRequest request = new CreateTopicRequest()
                    .withName(snsTopicName);
        CreateTopicResult result = amazonSNS.createTopic(request);
        Map<String, String> response = new HashMap<>();
        response.put("topicArn", result.getTopicArn());
		return Response.ok(response).build();
	}

    @Path("/getTopics")
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTopics() {
        List<Topic> topics = amazonSNS.listTopics().getTopics();
        Map<String, List<Topic>> response = new HashMap<>();
        response.put("snsTopics", topics);
        return Response.ok(response).build();
    }

    @Path("/subscribeQueue")
    @Timed
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscribeQueue(AmazonSNSRequest amazonSNSRequest) {
        logger.info("Request received is: {}", amazonSNSRequest.toString());
        Topics.subscribeQueue(amazonSNS, amazonSQS, amazonSNSRequest.getTopicArn(), amazonSNSRequest.getQueueUrl());
        Map<String, String> response = new HashMap<>();
        response.put("message", "SQS Queue is subscribed to SNS Topic successfully");
        return Response.ok(response).build();
    }

    @Path("/getSubscriptions")
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscriptions() {
        List<Subscription> subscriptions = amazonSNS.listSubscriptions().getSubscriptions();
        Map<String, List<Subscription>> response = new HashMap<>();
        response.put("snsSubscriptions", subscriptions);
        return Response.ok(response).build();
    }

    @Path("/publish")
    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response publish(AmazonSNSRequest amazonSNSRequest) {
        logger.info("Request received is: {}", amazonSNSRequest.toString() );
        PublishRequest publishRequest = new PublishRequest(amazonSNSRequest.getTopicArn(), amazonSNSRequest.getMessage())
                .withSubject(amazonSNSRequest.getSubject());
        PublishResult publishResult = amazonSNS.publish(publishRequest);
        Map<String, String> response = new HashMap<>();
        response.put("Message published successfully. Message ID is: ", publishResult.getMessageId());
        return Response.ok(response).build();
    }
}
