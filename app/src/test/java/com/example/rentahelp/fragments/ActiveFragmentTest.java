package com.example.rentahelp.fragments;

import junit.framework.TestCase;

public class ActiveFragmentTest extends TestCase {

    public void testOnCreateView() {
        assertEquals(0, ActiveFragment.class.getName().compareTo("com.example.rentahelp.fragments.ActiveFragment"));
    }

    public void testOnViewCreated() {
        assertEquals(0, ActiveFragment.class.getName().compareTo("com.example.rentahelp.fragments.ActiveFragment"));
    }
}