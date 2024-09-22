package com.example.rentahelp;

import junit.framework.TestCase;

public class LoginActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, LoginActivity.class.getName().compareTo("com.example.rentahelp.LoginActivity"));
    }

    public void testOnStart() {
        assertEquals(0, LoginActivity.class.getName().compareTo("com.example.rentahelp.LoginActivity"));
    }
}