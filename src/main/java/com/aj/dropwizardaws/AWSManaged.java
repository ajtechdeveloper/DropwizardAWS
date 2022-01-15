package com.aj.dropwizardaws;

import com.amazonaws.AmazonWebServiceClient;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWSManaged implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(AWSManaged.class);

    private AmazonWebServiceClient awsClient;

    private AWSManaged() {
        // Prevent instantiation
    }

    public AWSManaged(AmazonWebServiceClient awsClient) {
        if (awsClient == null) {
            throw new IllegalArgumentException("AWS client cannot be null");
        }
        this.awsClient = awsClient;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        logger.info("Shutting down AWS client, " + awsClient.getClass());
        awsClient.shutdown();
    }
}
