package com.examples.bindinterfacetoimpl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class StudentTest {
    private Person student;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new PersonModule());
        student = injector.getInstance(Person.class);
    }

    @Test
    public void receiveStudentName() {
        String name = student.getName();
        assertEquals("John", name);
    }
}
