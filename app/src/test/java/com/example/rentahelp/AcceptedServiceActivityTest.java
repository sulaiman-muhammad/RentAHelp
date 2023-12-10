package com.example.rentahelp;

import junit.framework.TestCase;

public class AcceptedServiceActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, AcceptedServiceActivity.class.getName().compareTo("com.example.rentahelp.AcceptedServiceActivity"));
    }
}