package com.example.rentahelp;

import junit.framework.TestCase;

public class NotificationActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, NotificationActivity.class.getName().compareTo("com.example.rentahelp.NotificationActivity"));
    }
}