package com.example.rentahelp;

import junit.framework.TestCase;

public class FindServiceActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, FindServiceActivity.class.getName().compareTo("com.example.rentahelp.FindServiceActivity"));
    }
}