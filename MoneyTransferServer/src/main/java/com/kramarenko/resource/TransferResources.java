package com.kramarenko.resource;

import com.codahale.metrics.annotation.Timed;
import com.kramarenko.model.Account;
import com.kramarenko.service.AccountService;
import com.kramarenko.service.TransferService;
import com.kramarenko.validation.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transfer-server")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TransferResources {
    private AccountService accountService;
    private TransferService transferService;

    @Inject
    public TransferResources(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @GET
    @Timed
    @Path("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GET
    @Timed
    @Path("/account")
    public Account getAccount(@QueryParam("id") Integer id) {
        if (id == null) {
            throw new WebApplicationException("Account from should not be null", Response.Status.BAD_REQUEST);
        }

        return accountService.getAccountById(id).orElse(null);
    }

    @POST
    @Timed
    @Path("/account")
    public Account createAccount(@QueryParam("name") String name, @QueryParam("amount") Double amount) {
        if (name == null) {
            throw new WebApplicationException("Account from should not be null", Response.Status.BAD_REQUEST);
        }

        if (amount == null) {
            throw new WebApplicationException("Account to should not be null", Response.Status.BAD_REQUEST);
        }

        return accountService.createAccount(name, amount);
    }

    @POST
    @Timed
    @Path("/transfer")
    public Response.Status transfer(@QueryParam("from") Integer from,
                            @QueryParam("to") Integer to,
                            @QueryParam("amount") Double amount) {
        if (from == null) {
            throw new WebApplicationException("Account from should not be null", Response.Status.BAD_REQUEST);
        }

        if (to == null) {
            throw new WebApplicationException("Account to should not be null", Response.Status.BAD_REQUEST);
        }

        if (amount == null) {
            throw new WebApplicationException("Amount should not be null", Response.Status.BAD_REQUEST);
        }

        Result result = transferService.transfer(from, to, amount);

        if (result.isFailure()) {
            throw new WebApplicationException(result.getErrorString(), Response.Status.NO_CONTENT);
        }

        return Response.Status.OK;
    }
}
