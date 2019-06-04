package com.kramarenko.resource;

import com.codahale.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/echo")
@Produces(MediaType.APPLICATION_JSON)
public class TransferResources {
    private final String defaultResponse;

    @Inject
    public TransferResources(String defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    @GET
    @Timed
    public String echo(@QueryParam("echo") Optional<String> echo) {
        return echo.orElse(defaultResponse);
    }
}
