package com.examples.providerbinding;

import com.google.inject.Provider;

import java.util.Date;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class MessageProvider implements Provider<String> {
    @Override
    public String get() {
       return "I am message";
    }
}
