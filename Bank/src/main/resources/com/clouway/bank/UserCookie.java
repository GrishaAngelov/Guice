package com.clouway.bank;

import javax.servlet.http.Cookie;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UserCookie extends Cookie {
    private String name;
    private String value;


    public UserCookie(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCookie)) return false;

        UserCookie cookie = (UserCookie) o;

        if (name != null ? !name.equals(cookie.name) : cookie.name != null) return false;
        if (value != null ? !value.equals(cookie.value) : cookie.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
