package com.examples.aop;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class StudentRegister {

   @Validate
    public String register(Student student) {
        return "Register: " + student.getName() + " is registered";
    }

}
