package com.examples.providerbinding;

import com.google.inject.AbstractModule;

import java.util.Date;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class MessageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).toProvider(MessageProvider.class);
    }
}
