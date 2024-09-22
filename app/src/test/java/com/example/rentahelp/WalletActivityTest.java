package com.example.rentahelp;

import junit.framework.TestCase;

public class WalletActivityTest extends TestCase {

    public void testOnCreate() {
        assertEquals(0, WalletActivity.class.getName().compareTo("com.example.rentahelp.WalletActivity"));
    }
}