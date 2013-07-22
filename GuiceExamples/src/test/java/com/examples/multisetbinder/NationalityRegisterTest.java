package com.examples.multisetbinder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
    public void receiveRegistrations() throws Exception {
        List<Citizen> citizenRegistrations = register.getCitizenList();
        List<String> nationalities = Arrays.asList("Bulgarian", "German", "Bulgarian");

        for (int i = 0; i < citizenRegistrations.size(); i++) {
            assertEquals(nationalities.get(i), citizenRegistrations.get(i).getNationality());
        }
    }
}
