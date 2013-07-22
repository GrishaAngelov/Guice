package com.clouway.bank;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

public class LoggedUsersCounter implements HttpSessionListener {
    private static Integer count = 0;

    public LoggedUsersCounter() {
    }

    public static int getCount() {
        return count;
    }

    @Override
    public void sessionCreated(HttpSessionEvent e) {
        synchronized (count) {
            count++;
//            e.getSession().setMaxInactiveInterval(60);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent e) {
        synchronized (count) {
            count--;
        }
    }
}


