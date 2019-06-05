package com.kramarenko.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kramarenko.model.Account;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class HttpWrapper {
    public static final int SERVER_ERROR = 500;
    public static final int OK = 200;

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = Logger.getLogger(HttpWrapper.class.getName());

    public static List<Account> getAllAccounts() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/transfer-server/accounts"))
                .build();
        HttpResponse<String> response;

        try {
            LOG.info("Sending request: " + request.uri().toString());
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<List<Account>>() {});
        } catch (IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }

    public static Optional<Account> getAccountById(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/transfer-server/account?id=" + id))
                .build();
        HttpResponse<String> response;

        try {
            LOG.info("Sending request: " + request.uri().toString());            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.of(objectMapper.readValue(response.body(), Account.class));
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public static int transfer(int from, int to, double amount) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/transfer-server/transfer?from=" +
                        from + "&to=" + to + "&amount=" + amount))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;

        try {
            LOG.info("Sending request: " + request.uri().toString());
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return SERVER_ERROR;
        }
        return response.statusCode();
    }
}
