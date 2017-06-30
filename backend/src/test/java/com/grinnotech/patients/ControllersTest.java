/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grinnotech.patients;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;

import static com.grinnotech.patients.WelcomeController.REPLY_WELCOME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author jacek.sztajnke
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
//@DirtiesContext
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class,
        locations = "classpath:/applicationContext.xml")
public class ControllersTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServletContext servletContext;

    public ControllersTest() {
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

    @Test
    public void testWelcome() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/welcome", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(REPLY_WELCOME, responseEntity.getBody());
    }

    @Test
    @Ignore
    public void testVersion() {

        assertNotNull(servletContext);
        assertNotNull(servletContext.getAttribute("servlet-context-attr"));
        assertEquals("test", servletContext.getAttribute("servlet-context-attr"));
    }
}
