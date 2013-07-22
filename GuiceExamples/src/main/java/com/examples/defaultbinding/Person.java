package com.examples.defaultbinding;

import com.google.inject.ImplementedBy;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@ImplementedBy(Citizen.class)
public interface Person {
    String getName();
}
