package com.examples.multimapbinder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

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
    public void receiveNationalities() throws Exception {
        Map<String, Citizen> nationalityMap = register.getNationalities();

        assertEquals("Bulgarian", nationalityMap.get("bg").getNationality());
        assertEquals("German",nationalityMap.get("ger").getNationality());
    }
}
