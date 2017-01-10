/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ricardo
 */
public class JobLoggerTest {
    
    public JobLoggerTest() {
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
     * Test of LogMessage method, of class JobLogger.
     */
    @Test
    public void testLogMessage() throws Exception {
        System.out.println("LogMessage");
        String messageText = "";
        boolean message = false;
        boolean warning = false;
        boolean error = false;
        JobLogger.LogMessage(messageText, message, warning, error);
    }
    
}
