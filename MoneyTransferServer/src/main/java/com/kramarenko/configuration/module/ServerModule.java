package com.kramarenko.configuration.module;

import com.google.inject.AbstractModule;
import com.kramarenko.dao.AccountDao;
import com.kramarenko.dao.MapAccountDao;

public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountDao.class).to(MapAccountDao.class);
    }
}
