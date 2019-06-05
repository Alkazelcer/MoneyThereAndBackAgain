package com.kramarenko.resource;

import com.codahale.metrics.annotation.Timed;
import com.kramarenko.model.Account;
import com.kramarenko.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TransferResources {
    private final String defaultResponse;
    private AccountService accountService;

    @Inject
    public TransferResources(String defaultResponse, AccountService accountService) {
        this.defaultResponse = defaultResponse;
        this.accountService = accountService;
    }

    @GET
    @Timed
    @Path("/echo")
    public String echo(@QueryParam("echo") Optional<String> echo) {
        return echo.orElse(defaultResponse);
    }

    @GET
    @Timed
    @Path("/accounts")
    public List<Account> accounts() {
        return accountService.getAccounts();
    }
}
