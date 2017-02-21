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
package com.grinnotech.patients.test;

import com.grinnotech.patients.model.Foo;
import com.grinnotech.patients.model.ImmutableFoo;
import com.grinnotech.patients.service.FooRestService;
import com.grinnotech.patients.vo.Result;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Jacek Sztajnke
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class,
        locations = "classpath:/testApplicationContext.xml")
@Ignore
public class FooRestServiceTest {
    
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
    private FooRestService fooService;
    
//    /**
//     * Test of update method, of class FooRestServiceImpl.
//     */
//    @Test
//    public void testUpdate() {
//        System.out.println("update");
//        String idFoo = "";
//        FooRestServiceImpl instance = new FooRestServiceImpl();
//        Result<Foo> expResult = null;
//        Result<Foo> result = instance.update(idFoo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of destroy method, of class FooRestServiceImpl.
//     */
//    @Test
//    public void testDestroy() {
//        System.out.println("destroy");
//        String idFoo = "";
//        FooRestServiceImpl instance = new FooRestServiceImpl();
//        Result<Foo> expResult = null;
//        Result<Foo> result = instance.destroy(idFoo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of read method, of class FooRestServiceImpl.
     */
    @Test
    public void testRead() {
//        Foo foo = ImmutableFoo.builder().build();
        String idFoo = "";
//        Result<Foo> expResult = ResultFactory.getSuccessResult(new Foo());
        Result<Foo> result = fooService.read(idFoo);
        Assert.assertEquals(null, result.getData().getId());
    }

//    /**
//     * Test of findAll method, of class FooRestServiceImpl.
//     */
//    @Test
//    public void testFindAll() {
//        System.out.println("findAll");
//        FooRestServiceImpl instance = new FooRestServiceImpl();
//        Result<List<Foo>> expResult = null;
//        Result<List<Foo>> result = instance.findAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
