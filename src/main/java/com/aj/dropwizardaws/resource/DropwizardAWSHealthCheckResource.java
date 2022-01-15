package com.aj.dropwizardaws.resource;

import com.aj.dropwizardaws.DropwizardAWSConfiguration;
import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardAWSHealthCheckResource extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardAWSHealthCheckResource.class);

    private static String appName;

    public DropwizardAWSHealthCheckResource(DropwizardAWSConfiguration dropwizardAWSConfiguration){
       appName = dropwizardAWSConfiguration.getAppName();
    }

    @Override
    protected Result check() {
        logger.info("App Name is: {}", appName);
        if("DropwizardAWS".equalsIgnoreCase(appName)) {
            return Result.healthy();
        }
        return Result.unhealthy("DropwizardAWS Service is down");
    }
}