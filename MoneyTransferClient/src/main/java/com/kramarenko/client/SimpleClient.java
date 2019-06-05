package com.kramarenko.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kramarenko.infrastructure.HttpWrapper;
import com.kramarenko.model.Account;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class SimpleClient implements Client {
    private static final Logger LOG = Logger.getLogger(SimpleClient.class.getName());

    private final List<Account> accounts = new ArrayList<>();
    private final int accountSize = new Random().nextInt(4) + 1;

    public void init() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        Random rnd = new Random();

        for (int i = 0; i < accountSize; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/transfer-server/account?name=" +
                            UUID.randomUUID().toString() + "&amount=" + rnd.nextDouble() * 1000))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            LOG.info("Sending request to create new account: " + request.uri().toString());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Account acc = objectMapper.readValue(response.body(), Account.class);
            accounts.add(acc);
        }
    }

    @Override
    public void startTransfering() {
        for (Account acc : accounts) {
            new Thread(() -> {
                Account innerAcc = acc;
                var accounts = HttpWrapper.getAllAccounts();
                var counter = 0;
                var rnd = new Random();

                while (true) {
                    if (counter % 10 == 0) {
                        accounts = HttpWrapper.getAllAccounts();
                    }

                    var to = accounts.get(rnd.nextInt(accounts.size())).getId();

                    var amount = innerAcc.getAmount() / 2;
                    LOG.info("I am " + innerAcc.getId() + ": Trying to give " + amount + " money to " + to );
                    var code = HttpWrapper.transfer(innerAcc.getId(), to, amount);

                    if (code == HttpWrapper.OK) {
                        LOG.info("I am " + innerAcc.getId() + ": Transfer to " + to + " with amount " + amount + "successful");
                        innerAcc.setAmount(amount);

                    }

                    try {
                        LOG.info("I am " + innerAcc.getId() + ": To tired. Going to sleep");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
