/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ricardo
 */
public class JobLoggerOutputTest {

    public JobLoggerOutputTest() {
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
    public void testGetLoggerOutputs() {
        Assert.assertEquals(0, JobLoggerOutput.getLoggerOutputs(false, false, false).size());
        Assert.assertEquals(1, JobLoggerOutput.getLoggerOutputs(true, false, false).size());
        Assert.assertEquals(1, JobLoggerOutput.getLoggerOutputs(false, true, false).size());
        Assert.assertEquals(1, JobLoggerOutput.getLoggerOutputs(false, false, true).size());
        Assert.assertEquals(2, JobLoggerOutput.getLoggerOutputs(true, true, false).size());
        Assert.assertEquals(2, JobLoggerOutput.getLoggerOutputs(true, false, true).size());
        Assert.assertEquals(2, JobLoggerOutput.getLoggerOutputs(false, true, true).size());
        Assert.assertEquals(3, JobLoggerOutput.getLoggerOutputs(true, true, true).size());

    }
}
