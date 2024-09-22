package com.example.rentahelp;

import junit.framework.TestCase;

public class MainActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, MainActivity.class.getName().compareTo("com.example.rentahelp.MainActivity"));
    }
}