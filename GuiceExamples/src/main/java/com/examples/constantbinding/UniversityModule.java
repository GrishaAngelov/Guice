package com.examples.constantbinding;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UniversityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("uni")).toInstance("uni-vt");
//        bindConstant().annotatedWith(Names.named("uni")).to("uni-vt");
    }
}
