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
package com.grinnotech.patients.test;

import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.model.ContactMethod;
import com.grinnotech.patients.service.ContactService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import static org.junit.Assert.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 *
 * @author Jacek Sztajnke
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class
//        locations = "classpath:/testApplicationContext.xml"
)
@Ignore
public class ContactServiceTest {
    
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

    @Autowired
    private ContactService contactService;

    @Autowired
    RequestMappingHandlerAdapter adapter;

    /**
     * Test of read method, of class ContactService.
     */
    @Test
    public void testRead() {
//        Result<Foo> expResult = ResultFactory.getSuccessResult(new Foo());
//        Result<Foo> result = fooService.read(idFoo);
//        Assert.assertEquals(null, result.getData().getId());

//        System.out.println("read");
        ExtDirectStoreReadRequest request = null;
        ExtDirectStoreResult<ContactMethod> expResult = null;
        ExtDirectStoreResult<ContactMethod> result = contactService.read(request);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

//    /**
//     * Test of destroy method, of class ContactService.
//     */
//    @Test
//    public void testDestroy() {
//        System.out.println("destroy");
//        MongoUserDetails userDetails = null;
//        ContactMethod contact = null;
//        ContactService instance = new ContactService();
//        ExtDirectStoreResult<ContactMethod> expResult = null;
//        ExtDirectStoreResult<ContactMethod> result = instance.destroy(userDetails, contact);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class ContactService.
//     */
//    @Test
//    public void testUpdate() {
//        System.out.println("update");
//        MongoUserDetails userDetails = null;
//        ContactMethod contact = null;
//        ContactService instance = new ContactService();
//        ValidationMessagesResult<ContactMethod> expResult = null;
//        ValidationMessagesResult<ContactMethod> result = instance.update(userDetails, contact);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
