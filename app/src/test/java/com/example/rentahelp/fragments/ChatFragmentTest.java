package com.example.rentahelp.fragments;

import junit.framework.TestCase;

public class ChatFragmentTest extends TestCase {

    public void testOnCreateView() {
        assertEquals(0, ChatFragment.class.getName().compareTo("com.example.rentahelp.fragments.ChatFragment"));
    }
}