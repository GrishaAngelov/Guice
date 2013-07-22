package com.examples.singleton.lazy;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class StudentRegister implements PersonRegister {
    @Override
    public void register(String personName) {
        System.out.println(personName+" registered");
    }
}
