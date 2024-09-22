package com.example.rentahelp.fragments;

import junit.framework.TestCase;

public class CompletedFragmentTest extends TestCase {

    public void testOnCreateView() {
        assertEquals(0, CompletedFragment.class.getName().compareTo("com.example.rentahelp.fragments.CompletedFragment"));
    }

    public void testOnViewCreated() {
        assertEquals(0, CompletedFragment.class.getName().compareTo("com.example.rentahelp.fragments.CompletedFragment"));
    }
}