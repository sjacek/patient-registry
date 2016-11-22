/*
 * Copyright (C) 2016 Pivotal Software, Inc.
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

import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.model.Patient;
import com.grinno.patients.util.ValidationMessagesResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Jacek Sztajnke
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PatientServiceTest {
    
    public PatientServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of read method, of class PatientService.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        ExtDirectStoreReadRequest request = null;
        PatientService instance = null;
        ExtDirectStoreResult<Patient> expResult = null;
        ExtDirectStoreResult<Patient> result = instance.read(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class PatientService.
     */
    @Test
    public void testDestroy() {
        System.out.println("destroy");
        MongoUserDetails userDetails = null;
        Patient patient = null;
        PatientService instance = null;
        ExtDirectStoreResult<Patient> expResult = null;
        ExtDirectStoreResult<Patient> result = instance.destroy(userDetails, patient);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class PatientService.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        MongoUserDetails userDetails = null;
        Patient patient = null;
        PatientService instance = null;
        ValidationMessagesResult<Patient> expResult = null;
        ValidationMessagesResult<Patient> result = instance.update(userDetails, patient);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
