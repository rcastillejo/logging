/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.core.Is;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Ricardo
 */
public class JobLoggerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        Map configuration = new HashMap();
        configuration.put("userName", "root");
        configuration.put("password", "1234");
        configuration.put("dbms", "postgresql");
        configuration.put("serverName", "localhost");
        configuration.put("portNumber", "5432");
        configuration.put("logFileFolder", "D:");
        JobLogger jobLogger = new JobLogger(false, false, false, false, false, false, null);
    }

    @Test
    public void testLogMessageEmpty() throws Exception {
        System.out.println("LogMessage");
        String messageText = "";
        boolean message = false;
        boolean warning = false;
        boolean error = false;
        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testLogMessageWithNoLevels() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean message = false;
        boolean warning = false;
        boolean error = false;

        expectedException.expectMessage("Invalid configuration");

        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testLogMessageWithNoLevelSpecified() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean message = false;
        boolean warning = false;
        boolean error = false;
        expectedException.expectMessage("Error or Warning or Message must be specified");

        JobLogger jobLogger = new JobLogger(true, true, true, true, true, true, null);
        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testLogMessageConsole() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = true;
        boolean warning = true;
        boolean error = true;
        Map databaseConfiguration = null;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, databaseConfiguration);

        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testLogMessageFile() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToFile = true;
        boolean logToConsole = false;
        boolean logToDatabase = false;
        boolean message = true;
        boolean warning = false;
        boolean error = false;
        Map configuration = new HashMap();
        String folder = ".";
        configuration.put("logFileFolder", folder);

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, configuration);

        JobLogger.logBasedOnLevel(messageText, message, warning, error);

        assertTrue(new File(folder, "logFile.txt").exists());
    }

    @Ignore
    @Test
    public void testLogMessageDataBase() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToFile = false;
        boolean logToConsole = false;
        boolean logToDatabase = true;
        boolean message = true;
        boolean warning = false;
        boolean error = false;
        Map configuration = new HashMap();
        configuration.put("userName", "root");
        configuration.put("password", "1234");
        configuration.put("dbms", "oracle");
        configuration.put("serverName", "oracle");
        configuration.put("portNumber", "1521");

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, configuration);

        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testFormatMessage() {
        boolean message = true;
        boolean warning = false;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(true, true, true, true, true, true, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("message "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageWarning() {
        boolean message = false;
        boolean warning = true;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(true, true, true, true, true, true, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("warning "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageError() {
        boolean message = false;
        boolean warning = false;
        boolean error = true;

        JobLogger jobLogger = new JobLogger(true, true, true, true, true, true, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatWarningAndError() {
        boolean message = false;
        boolean warning = true;
        boolean error = true;

        JobLogger jobLogger = new JobLogger(true, true, true, true, true, true, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.contains("Hola Mundowarning"));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

}
