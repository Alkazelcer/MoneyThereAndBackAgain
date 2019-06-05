package com.kramarenko;

import com.kramarenko.client.SimpleClient;

import java.io.IOException;

public class ClientRunner {

    public static void main(String[] args) throws IOException, InterruptedException {
        SimpleClient client = new SimpleClient();

        client.init();

        client.startTransfering();
    }
}
