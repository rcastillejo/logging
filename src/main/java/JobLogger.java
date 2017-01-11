/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

    private static boolean shouldLogInFile;
    private static boolean shouldLogInConsole;
    private static boolean isMessage;
    private static boolean isWarning;
    private static boolean isError;
    private static boolean shouldLogInDataBase;
    private boolean initialized;
    private static Map configuration;
    private static Logger logger;

    public JobLogger(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase,
            boolean isMessage, boolean isWarning, boolean isError, Map configuration) {
        logger = Logger.getLogger("MyLog");
        JobLogger.isError = isError;
        JobLogger.isMessage = isMessage;
        JobLogger.isWarning = isWarning;
        JobLogger.shouldLogInDataBase = shouldLogInDataBase;
        JobLogger.shouldLogInFile = shouldLogInFile;
        JobLogger.shouldLogInConsole = shouldLogInConsole;
        JobLogger.configuration = configuration;
    }

    public static void logBasedOnLevel(String messageText, boolean isMessage, boolean isWarning, boolean isError) throws Exception {
        messageText.trim();
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        if (!JobLogger.shouldLogInConsole && !JobLogger.shouldLogInFile && !JobLogger.shouldLogInDataBase) {
            throw new Exception("Invalid configuration");
        }
        if ((!JobLogger.isError && !JobLogger.isMessage && !JobLogger.isWarning) || (!isMessage && !isWarning && !isError)) {
            throw new Exception("Error or Warning or Message must be specified");
        }

        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", JobLogger.configuration.get("userName"));
        connectionProps.put("password", JobLogger.configuration.get("password"));

        connection = DriverManager.getConnection("jdbc:" + JobLogger.configuration.get("dbms") + "://" + JobLogger.configuration.get("serverName")
                + ":" + JobLogger.configuration.get("portNumber") + "/", connectionProps);

        int levelCode = 0;
        if (isMessage && JobLogger.isMessage) {
            levelCode = 1;
        }

        if (isError && JobLogger.isError) {
            levelCode = 2;
        }

        if (isWarning && JobLogger.isWarning) {
            levelCode = 3;
        }

        Statement statement = connection.createStatement();

        String textToLog = null;
        File logFile = new File(JobLogger.configuration.get("logFileFolder") + "/logFile.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        FileHandler fileHandler = new FileHandler(JobLogger.configuration.get("logFileFolder") + "/logFile.txt");
        ConsoleHandler consoleHandler = new ConsoleHandler();

        if (isError && JobLogger.isError) {
            textToLog = textToLog + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (isWarning && JobLogger.isWarning) {
            textToLog = textToLog + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (isMessage && JobLogger.isMessage) {
            textToLog = textToLog + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (JobLogger.shouldLogInFile) {
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, messageText);
        }

        if (JobLogger.shouldLogInConsole) {
            logger.addHandler(consoleHandler);
            logger.log(Level.INFO, messageText);
        }

        if (JobLogger.shouldLogInDataBase) {
            statement.executeUpdate("insert into Log_Values('" + isMessage + "', " + String.valueOf(levelCode) + ")");
        }
    }
}
