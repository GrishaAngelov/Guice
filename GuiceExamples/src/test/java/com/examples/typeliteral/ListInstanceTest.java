package com.examples.typeliteral;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class ListInstanceTest {
    private ListInstance instance;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new ListModule());
        instance = injector.getInstance(ListInstance.class);
    }

    @Test
    public void checkIsArrayListInstance() throws Exception {
      assertTrue(instance.isArrayListInstance());
    }
}
