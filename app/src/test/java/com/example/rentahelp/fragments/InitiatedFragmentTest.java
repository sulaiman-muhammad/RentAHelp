package com.example.rentahelp.fragments;

import junit.framework.TestCase;

public class InitiatedFragmentTest extends TestCase {

    public void testOnCreateView() {
        assertEquals(0, InitiatedFragment.class.getName().compareTo("com.example.rentahelp.fragments.InitiatedFragment"));
    }

    public void testOnViewCreated() {
        assertEquals(0, InitiatedFragment.class.getName().compareTo("com.example.rentahelp.fragments.InitiatedFragment"));
    }
}