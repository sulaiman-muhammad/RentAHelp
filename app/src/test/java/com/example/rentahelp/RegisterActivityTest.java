package com.example.rentahelp;

import junit.framework.TestCase;

public class RegisterActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, RegisterActivity.class.getName().compareTo("com.example.rentahelp.RegisterActivity"));
    }
}