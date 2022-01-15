package com.aj.dropwizardaws;

import com.aj.dropwizardaws.resource.AmazonSNSResource;
import com.aj.dropwizardaws.resource.AmazonSQSResource;
import com.aj.dropwizardaws.resource.DropwizardAWSHealthCheckResource;
import com.aj.dropwizardaws.resource.PingResource;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DropwizardAWSApplication extends Application<DropwizardAWSConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardAWSApplication.class);

    public static void main(String[] args) throws Exception {
        new DropwizardAWSApplication().run("server", args[0]);
    }

    @Override
    public void initialize(Bootstrap<DropwizardAWSConfiguration> b) {
    }

    @Override
    public void run(DropwizardAWSConfiguration config, Environment env) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("ajtechdeveloper", "softwaredevelopercentral");
        logger.info("Initialize Amazon SNS entry point");
        AmazonSNS sns = AmazonSNSClientBuilder.standard().
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://192.168.99.100:4566", "us-east-1")).
                withCredentials(new AWSStaticCredentialsProvider(awsCreds)).
                build();
        env.lifecycle().manage(new AWSManaged((AmazonWebServiceClient) sns));
        logger.info("Initialize Amazon SQS entry point");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard().
                withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://192.168.99.100:4566", "us-east-1")).
                withCredentials(new AWSStaticCredentialsProvider(awsCreds)).
                build();
        env.lifecycle().manage(new AWSManaged((AmazonWebServiceClient) sqs));
        logger.info("Registering RESTful API resources");
        env.jersey().register(new PingResource());
        env.jersey().register(new AmazonSQSResource(sqs));
        env.jersey().register(new AmazonSNSResource(sns, sqs));
        env.healthChecks().register("DropwizardAWSHealthCheck",
                new DropwizardAWSHealthCheckResource(config));
    }
}
