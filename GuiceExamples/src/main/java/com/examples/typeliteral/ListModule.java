package com.examples.typeliteral;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class ListModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<List<String>>(){}).to(new TypeLiteral<ArrayList<String>>(){});
    }
}
