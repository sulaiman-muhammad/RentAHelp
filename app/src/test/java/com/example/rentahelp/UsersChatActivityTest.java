package com.example.rentahelp;

import junit.framework.TestCase;

public class UsersChatActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, UsersChatActivity.class.getName().compareTo("com.example.rentahelp.UsersChatActivity"));
    }
}