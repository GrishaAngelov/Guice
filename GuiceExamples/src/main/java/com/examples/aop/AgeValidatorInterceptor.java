package com.examples.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class AgeValidatorInterceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object msg;
        Object[] methodArgs = methodInvocation.getArguments();
        Student student = ((Student) methodArgs[0]);
        int age = student.getAge();

        if (age > 18) {
            msg = methodInvocation.proceed();
        } else {
            msg = "Register: " + student.getName() + " is not registered";
        }
        return msg;
    }
}
