package com.kramarenko.configuration.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class TransferServerConfiguration extends Configuration {
    @NotEmpty
    private String defaultResponse = "I dont know who you are";

    @JsonProperty
    public String getdefaultResponse() {
        return defaultResponse;
    }

    @JsonProperty
    public void setdefaultResponse(String defaultResponse) {
        this.defaultResponse = defaultResponse;
    }
}