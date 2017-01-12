/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class JobLoggerLevelTest {

    public JobLoggerLevelTest() {
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
    public void testGetLevel() {
        Assert.assertEquals(JobLoggerLevel.MESSAGE, JobLoggerLevel.getLoggerLevel(true, false, false));
        Assert.assertEquals(JobLoggerLevel.MESSAGE, JobLoggerLevel.getLoggerLevel(true, true, false));
        Assert.assertEquals(JobLoggerLevel.MESSAGE, JobLoggerLevel.getLoggerLevel(true, false, true));
        Assert.assertEquals(JobLoggerLevel.MESSAGE, JobLoggerLevel.getLoggerLevel(true, true, true));

        Assert.assertEquals(JobLoggerLevel.WARNING, JobLoggerLevel.getLoggerLevel(false, true, false));
        Assert.assertEquals(JobLoggerLevel.WARNING, JobLoggerLevel.getLoggerLevel(false, true, true));
        Assert.assertEquals(JobLoggerLevel.ERROR, JobLoggerLevel.getLoggerLevel(false, false, true));
    }

}
