package com.examples.defaultbinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class CitizenTest {
    private Person citizen;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector();
        citizen = injector.getInstance(Person.class);
    }

    @Test
    public void receiveName() throws Exception {
        String name = citizen.getName();

        assertEquals("John", name);
    }
}
