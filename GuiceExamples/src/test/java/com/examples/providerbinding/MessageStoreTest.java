package com.examples.providerbinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class MessageStoreTest {
    private MessageStore messageStore;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new MessageModule());
        messageStore = injector.getInstance(MessageStore.class);
    }

    @Test
    public void receiveMessage() throws Exception {
        String msg = messageStore.getMessage();
        assertEquals("I am message",msg);
    }
}
