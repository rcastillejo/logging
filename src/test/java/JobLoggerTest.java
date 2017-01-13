/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Ricardo
 */
public class JobLoggerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInstanceWithNoOutputs() throws Exception {
        System.out.println("LogMessage");
        boolean logToConsole = false;
        boolean logToFile = false;
        boolean logToDatabase = false;
        boolean message = false;
        boolean warning = false;
        boolean error = false;

        expectedException.expectMessage("Invalid configuration");

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);
    }

    @Test
    public void testInstanceWithAllLevelsOff() throws Exception {
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
    }

    @Test
    public void testConfigWithNoOutputs() throws Exception {
        System.out.println("LogMessage");
        JobLoggerLevel level = null;
        Map configuration = null;
        List<JobLoggerOutput> outputs = null;

        expectedException.expectMessage("Invalid configuration");

        JobLogger.config(level, configuration, outputs);
    }

    @Test
    public void testConfigWithOutLevels() throws Exception {
        System.out.println("LogMessage");
        JobLoggerLevel level = null;
        Map configuration = null;
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        expectedException.expectMessage("Error or Warning or Message must be specified");

        JobLogger.config(level, configuration, outputs);
    }

    @Test
    public void testLogMessageEmpty() throws Exception {
        System.out.println("LogMessage");
        String messageText = "";
        boolean message = false;
        boolean warning = false;
        boolean error = false;

        JobLogger.LogMessage(messageText, message, warning, error);
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

        JobLogger.addLoggerOutput(JobLoggerOutput.CONSOLE);

        JobLogger.LogMessage(messageText, message, warning, error);
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
        JobLogger.addLoggerOutput(JobLoggerOutput.CONSOLE);
        JobLogger.LogMessage(messageText, message, warning, error);

        assertTrue(new File(folder, "logFile.txt").exists());
    }

    @Test
    public void testLogMessageDataBase() throws Exception {
        System.out.println("LogMessage");
        String messageText = "Hola Mundo";
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
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

        DummyDataBaseHandler handler = new DummyDataBaseHandler();
        JobLogger.configAndAddOutput(handler);
        JobLogger.LogMessage(messageText, message, warning, error);

        String sqlCommand = handler.getSqlCommand();
        System.out.println("sqlCommand: " + sqlCommand);

        Assert.assertTrue(sqlCommand.startsWith("insert into Log_Values('message "));
        Assert.assertTrue(sqlCommand.endsWith("Hola Mundo', 1)"));
    }

    @Test
    public void testFormatMessageWithLevelsFlags() {
        boolean logToFile = false;
        boolean logToConsole = true;
        boolean logToDatabase = false;
        boolean message = true;
        boolean warning = false;
        boolean error = false;

        JobLogger jobLogger = new JobLogger(logToFile, logToConsole, logToDatabase, message, warning, error, null);

        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(message, warning, error);

        String result = JobLogger.formatTextLog("Hola Mundo", level);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("message "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageWithLevelConstants() {
        String result;
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        JobLogger.config(JobLoggerLevel.MESSAGE, null, outputs);

        result = JobLogger.formatTextLog("Hola Mundo", JobLoggerLevel.MESSAGE);
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

        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(message, warning, error);
        String result = JobLogger.formatTextLog("Hola Mundo", level);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("warning "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageWarningWithConstants() {
        String result;
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        JobLogger.config(JobLoggerLevel.MESSAGE, null, outputs);

        result = JobLogger.formatTextLog("Hola Mundo", JobLoggerLevel.WARNING);
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

        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(message, warning, error);
        String result = JobLogger.formatTextLog("Hola Mundo", level);
        System.out.println("result: " + result);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }

    @Test
    public void testFormatMessageErrorConstants() {
        String result;
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        JobLogger.config(JobLoggerLevel.MESSAGE, null, outputs);

        result = JobLogger.formatTextLog("Hola Mundo", JobLoggerLevel.ERROR);
        Assert.assertTrue(result.startsWith("error "));
        Assert.assertTrue(result.endsWith("Hola Mundo"));
    }
    
    
    @Test
    public void testLogMessageConsoleWarningDiffLevels() throws Exception {
        String result;
        DummyOutput handler = new DummyOutput();
        
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        JobLogger.config(JobLoggerLevel.WARNING, null, outputs);
        JobLogger.configAndAddOutput(handler);

        JobLogger.logBasedOnLevel("Hola Mundo Message", JobLoggerLevel.MESSAGE);
        assertNull(handler.getMessage());

        JobLogger.logBasedOnLevel("Hola Mundo Warning", JobLoggerLevel.WARNING);
        assertTrue(handler.getMessage().contains("Warning"));

        JobLogger.logBasedOnLevel("Hola Mundo Error", JobLoggerLevel.ERROR);
        assertTrue(handler.getMessage().contains("Error"));
    }
    
    @Test
    public void testLogMessageConsoleErrorDiffLevels() throws Exception {
        String result;
        DummyOutput handler = new DummyOutput();
        
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        outputs.add(JobLoggerOutput.CONSOLE);

        JobLogger.config(JobLoggerLevel.ERROR, null, outputs);
        JobLogger.configAndAddOutput(handler);

        JobLogger.logBasedOnLevel("Hola Mundo Message", JobLoggerLevel.MESSAGE);
        assertNull(handler.getMessage());

        JobLogger.logBasedOnLevel("Hola Mundo Warning", JobLoggerLevel.WARNING);
        assertNull(handler.getMessage());

        JobLogger.logBasedOnLevel("Hola Mundo Error", JobLoggerLevel.ERROR);
        assertTrue(handler.getMessage().contains("Error"));
    } 

}
