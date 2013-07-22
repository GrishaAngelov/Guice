package com.examples.bindinterfacetoimpl;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class Student implements Person {
    @Override
    public String getName() {
        return "John";
    }
}
