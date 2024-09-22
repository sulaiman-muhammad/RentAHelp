package com.example.rentahelp;

import junit.framework.TestCase;

public class AccountActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, AccountActivity.class.getName().compareTo("com.example.rentahelp.AccountActivity"));
    }
}