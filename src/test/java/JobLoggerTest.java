/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        JobLogger.createLogger();
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
    public void testLogMessageWithNoHandlers() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToConsole = false;
        boolean logToFile = false;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = false;
        boolean error = false;

        expectedException.expectMessage("Invalid configuration");

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);
        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Test
    public void testLogMessageWithNoLevelSpecified() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToConsole = true;
        boolean logToFile = false;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = false;
        boolean error = false;
        expectedException.expectMessage("Error or Warning or Message must be specified");

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);
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

        JobLogger.addHandlder(new ConsoleHandler());

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

        JobFileHandler handler = new JobFileHandler(configuration);

        JobLogger.addHandlder(handler);
        JobLogger.logBasedOnLevel(messageText, message, warning, error);

        assertEquals("./logFile.txt", handler.getPathname());
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

        DummyDataBaseHandler handler = new DummyDataBaseHandler(configuration);
        JobLogger.addHandlder(handler);
        JobLogger.logBasedOnLevel(messageText, message, warning, error);

        String sqlCommand = handler.getSqlCommand();
        System.out.println("sqlCommand: " + sqlCommand);

        Assert.assertTrue(sqlCommand.startsWith("insert into Log_Values('message "));
        Assert.assertTrue(sqlCommand.endsWith("Hola Mundo',1);"));
    }

    @Test
    public void testFormatMessage() {
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = true;
        boolean warning = false;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("message "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageWarning() {
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = true;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("warning "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageError() {
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = false;
        boolean error = true;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, warning, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatWarningAndError() {
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = false;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, true, true, true, null);

        String result = JobLogger.formatTextLog("Hola Mundo", message, true, error);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("warning "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));

        result = JobLogger.formatTextLog("Hola Mundo", message, warning, true);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

}
