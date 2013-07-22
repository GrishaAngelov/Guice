package com.examples.singleton.eager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class StudentRegisterTest {
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new RegisterModule());
    }

    @Test
    public void receiveSameInstance() throws Exception {
        PersonRegister firstInstance = injector.getInstance(PersonRegister.class);
        PersonRegister secondInstance = injector.getInstance(PersonRegister.class);

        assertThat(firstInstance,is(sameInstance(secondInstance)));
    }
}
