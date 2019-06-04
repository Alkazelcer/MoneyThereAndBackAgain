package com.kramarenko;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kramarenko.configuration.dropwizard.TransferServerConfiguration;
import com.kramarenko.configuration.module.ServerModule;
import com.kramarenko.healthcheck.TemplateHealthCheck;
import com.kramarenko.resource.TransferResources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TransferServer extends Application<TransferServerConfiguration> {
    public static void main(String[] args) throws Exception {
        new TransferServer().run(args);
    }

    @Override
    public String getName() {
        return "Money there and back again";
    }

    @Override
    public void initialize(Bootstrap<TransferServerConfiguration> bootstrap) {
    }

    @Override
    public void run(TransferServerConfiguration configuration, Environment environment) {
        Injector injector = Guice.createInjector(new ServerModule());

        final TransferResources resource = new TransferResources (configuration.getdefaultResponse());
        environment.jersey().register(resource);

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck();
        environment.healthChecks().register("template", healthCheck);
    }
}
