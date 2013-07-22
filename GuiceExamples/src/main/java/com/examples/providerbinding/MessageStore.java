package com.examples.providerbinding;

import com.google.inject.Inject;

import java.util.Date;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class MessageStore {
    private MessageProvider messageProvider;

    @Inject
    public MessageStore(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    public String getMessage(){
        return messageProvider.get();
    }
}
