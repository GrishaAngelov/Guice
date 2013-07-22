package com.examples.aop;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class StudentRegisterTest {
    private StudentRegister register;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new AgeModule());
        register = injector.getInstance(StudentRegister.class);
    }

    @Test
    public void happyPath() throws Exception {
        String message = register.register(new Student("John", 20));
        assertEquals("Register: John is registered", message);
    }

    @Test
    public void registerStudentUnderRequiredAge() throws Exception {
        String message = register.register(new Student("John", 16));
        assertEquals("Register: John is not registered", message);
    }
}
