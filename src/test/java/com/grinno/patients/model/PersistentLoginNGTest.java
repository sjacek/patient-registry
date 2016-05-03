/*
 * Copyright (C) 2016 jacek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.grinno.patients.model;

import java.util.Date;
import static org.testng.Assert.*;

/**
 *
 * @author jacek
 */
public class PersistentLoginNGTest {
    
    public PersistentLoginNGTest() {
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getSeries method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetSeries() {
        System.out.println("getSeries");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getSeries();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setSeries method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetSeries() {
        System.out.println("setSeries");
        String series = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setSeries(series);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getToken method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetToken() {
        System.out.println("getToken");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getToken();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setToken method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetToken() {
        System.out.println("setToken");
        String token = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setToken(token);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastUsed method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetLastUsed() {
        System.out.println("getLastUsed");
        PersistentLogin instance = new PersistentLogin();
        Date expResult = null;
        Date result = instance.getLastUsed();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setLastUsed method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetLastUsed() {
        System.out.println("setLastUsed");
        Date lastUsed = null;
        PersistentLogin instance = new PersistentLogin();
//        instance.setLastUsed(lastUsed);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getIpAddress method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetIpAddress() {
        System.out.println("getIpAddress");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getIpAddress();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setIpAddress method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetIpAddress() {
        System.out.println("setIpAddress");
        String ipAddress = "";
        PersistentLogin instance = new PersistentLogin();
//        instance.setIpAddress(ipAddress);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserAgent method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetUserAgent() {
        System.out.println("getUserAgent");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getUserAgent();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserAgent method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetUserAgent() {
        System.out.println("setUserAgent");
        String userAgent = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setUserAgent(userAgent);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserId method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetUserId() {
        System.out.println("getUserId");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getUserId();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserId method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetUserId() {
        System.out.println("setUserId");
        String userId = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setUserId(userId);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserAgentName method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetUserAgentName() {
        System.out.println("getUserAgentName");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getUserAgentName();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserAgentName method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetUserAgentName() {
        System.out.println("setUserAgentName");
        String userAgentName = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setUserAgentName(userAgentName);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserAgentVersion method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetUserAgentVersion() {
        System.out.println("getUserAgentVersion");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getUserAgentVersion();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserAgentVersion method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetUserAgentVersion() {
        System.out.println("setUserAgentVersion");
        String userAgentVersion = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setUserAgentVersion(userAgentVersion);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getOperatingSystem method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testGetOperatingSystem() {
        System.out.println("getOperatingSystem");
        PersistentLogin instance = new PersistentLogin();
        String expResult = "";
        String result = instance.getOperatingSystem();
//        assertEquals(result, expResult);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setOperatingSystem method, of class PersistentLogin.
     */
    @org.testng.annotations.Test
    public void testSetOperatingSystem() {
        System.out.println("setOperatingSystem");
        String operatingSystem = "";
        PersistentLogin instance = new PersistentLogin();
        instance.setOperatingSystem(operatingSystem);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
