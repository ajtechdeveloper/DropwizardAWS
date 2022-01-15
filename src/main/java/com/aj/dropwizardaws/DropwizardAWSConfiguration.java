package com.aj.dropwizardaws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DropwizardAWSConfiguration extends Configuration {

    @JsonProperty
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
