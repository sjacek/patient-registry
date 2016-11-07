/*
 * Copyright (C) 2016 Jacek Sztajnke
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
package com.grinno.patients.service;

import java.util.HashMap;
import java.util.Map;
import static org.testng.Assert.*;

/**
 *
 * @author jacek
 */
public class LogServiceNGTest {
    
    public LogServiceNGTest() {
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
     * Test of logClientCrash method, of class LogService.
     */
    @org.testng.annotations.Test
    public void testLogClientCrash() {
        System.out.println("logClientCrash");
        String userAgent = "";
        Map<String, Object> crashData = new HashMap<String, Object>() {{ put("msg","test"); }};
        LogService instance = new LogService();
        instance.logClientCrash(userAgent, crashData);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        assertTrue(true);
    }

    /**
     * Test of info method, of class LogService.
     */
    @org.testng.annotations.Test
    public void testInfo() {
        System.out.println("info");
        String message = "";
        LogService instance = new LogService();
        instance.info(message);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        assertTrue(true);
    }

    /**
     * Test of debug method, of class LogService.
     */
    @org.testng.annotations.Test
    public void testDebug() {
        System.out.println("debug");
        String message = "";
        LogService instance = new LogService();
        instance.debug(message);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        assertTrue(true);
    }
    
}
