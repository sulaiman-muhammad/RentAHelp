package com.example.rentahelp;

import junit.framework.TestCase;

public class PostedServiceActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, PostedServiceActivity.class.getName().compareTo("com.example.rentahelp.PostedServiceActivity"));
    }
}