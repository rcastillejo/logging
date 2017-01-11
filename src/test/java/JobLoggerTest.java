/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Map;
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

    @Ignore
    @Test
    public void testLogMessageConsole() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = true;
        boolean warning = false;
        boolean error = false;
        Map databaseConfiguration = null;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, databaseConfiguration);

        JobLogger.logBasedOnLevel(messageText, message, warning, error);
    }

    @Ignore
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
        configuration.put("logFileFolder", ".");

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, configuration);

        JobLogger.logBasedOnLevel(messageText, message, warning, error);
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

}
