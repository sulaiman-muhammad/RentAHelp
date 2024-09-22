package com.example.rentahelp;

import junit.framework.TestCase;

public class ChatActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, ChatActivity.class.getName().compareTo("com.example.rentahelp.ChatActivity"));
    }
}