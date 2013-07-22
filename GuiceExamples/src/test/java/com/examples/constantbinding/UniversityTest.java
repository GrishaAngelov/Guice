package com.examples.constantbinding;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UniversityTest {
    private University university;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new UniversityModule());
        university = injector.getInstance(University.class);
    }

    @Test
    public void receiveUniversityName() {
        String uniName = university.getUniversityName();
        assertEquals("uni-vt", uniName);
    }
}
