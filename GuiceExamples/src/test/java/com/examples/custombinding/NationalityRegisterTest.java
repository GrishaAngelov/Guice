package com.examples.custombinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegisterTest {
    private NationalityRegister register;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new NationalityModule());
        register = injector.getInstance(NationalityRegister.class);
    }

    @Test
    public void receiveNationality() throws Exception {
        String nationality = register.getNationality();
        assertEquals("Bulgarian", nationality);
    }
}
