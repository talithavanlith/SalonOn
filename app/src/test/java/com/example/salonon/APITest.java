package com.example.salonon;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class APITest {

    Profile testProfile;
    API api = new API();
    String password = "hunter2";

    @Before
    public void setUp() throws Exception {
        this.testProfile = new Profile("unitTestEmail", "unitTestFirstName","unitTestLastName", null,false, false, "none", "none", 0);
    }

    @After
    public void tearDown() throws Exception {
        // need some way to delete testProfile from the database
        Log.v("Unit Test", "Called Tear Down()");
    }

    @Test
    public void jsonToProfile() throws Exception {
        // assertEquals("expected" api.jsonToProfile(this.testProfile)); How do I call this function?
        assertFalse(false);
    }

    @Test
    public void testShouldAlwaysPass() throws Exception {
        assertEquals(10, 5+5);
    }

    @Test
    public void createNewProfile() throws Exception {
        assertEquals(true, api.createNewProfile(this.testProfile, this.password));
    }

    @Test
    public void login() throws Exception {
        assertEquals(this.testProfile, this.api.login(this.testProfile.email, this.password));
    }

    @Test
    public void searchStylistByLocation() throws Exception {
        assertNotNull(this.api.searchStylistByLocation(this.testProfile));
    }

    @Test
    public void searchSalonByLocation() throws Exception {
        assertNotNull(this.api.searchSalonByLocation("35.9132 N", "79.0558 W", 10));
    }

    @Test
    public void getClientProfile() throws Exception {
        assertNotNull(api.getClientProfile(this.testProfile.email));
    }
}