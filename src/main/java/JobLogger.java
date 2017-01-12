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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
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
        //public static void logBasedOnLevel(String messageText, JobLoggerLevel level) throws Exception {
        if (isMessageEmpty(messageText)) {
            return;
        }
        validateConfiguration();
        validateLevel(isMessage, isWarning, isError);

        messageText = messageText.trim();

        String textToLog = formatTextLog(messageText, isMessage, isWarning, isError);

        if (isShouldLogInFile()) {
            showLogInFile(textToLog);
        }

        if (isShouldLogInConsole()) {
            showLogInConsole(textToLog);
        }

        if (isShouldLogInDataBase()) {
            showLogInDataBase(textToLog);
        }
    }

    public static String formatTextLog(String messageText, boolean isMessage, boolean isWarning, boolean isError) {
        String textToLog = "";
        if (isError && JobLogger.isError) {
            textToLog = textToLog + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (isWarning && JobLogger.isWarning) {
            textToLog = textToLog + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (isMessage && JobLogger.isMessage) {
            textToLog = textToLog + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }
        return textToLog;
    }

    private static void showLogInConsole(String messageText) {
        addHandlerAndLog(getConsoleHandler(), messageText);
    }

    private static Handler getConsoleHandler() {
        return new ConsoleHandler();
    }

    private static void showLogInFile(String messageText) throws IOException, SecurityException {
        String pathname = JobLogger.configuration.get("logFileFolder") + "/logFile.txt";

        verifyLogFile(pathname);

        addHandlerAndLog(getFileHandler(pathname), messageText);
    }

    private static void verifyLogFile(String pathname) throws IOException {
        File logFile = new File(pathname);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }

    private static Handler getFileHandler(String pathname) throws IOException {
        return new FileHandler(pathname);
    }

    private static void addHandlerAndLog(Handler handler, String messageText) {
        logger.addHandler(handler);
        logger.log(Level.INFO, messageText);
        handler.close();
    }

    
    private static void showLogInDataBase(String messageText) throws SQLException {
        //@TODO: Crear un Handler para la salida de los mensajes para la bd.
        Connection connection = null;
        int levelCode;

        try {
            connection = getConnection();

            levelCode = getLevelCode();

            Statement statement = connection.createStatement();

            statement.executeUpdate(createSqlStatement(isMessage, levelCode));
        } finally {
            closeConnection(connection);
        }
    }

    private static Connection getConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", JobLogger.configuration.get("userName"));
        connectionProps.put("password", JobLogger.configuration.get("password"));

        Connection connection = DriverManager.getConnection("jdbc:" + JobLogger.configuration.get("dbms") + "://" + JobLogger.configuration.get("serverName")
                + ":" + JobLogger.configuration.get("portNumber") + "/", connectionProps);

        return connection;
    }

    private static String createSqlStatement(boolean isMessage, int levelCode) {
        return "insert into Log_Values('" + isMessage + "', " + String.valueOf(levelCode) + ")";
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                Logger.getLogger(JobLogger.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }

    private static int getLevelCode() {
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
        return levelCode;
    }

    private static void validateConfiguration() throws Exception {
        if (!isShouldLogInConsole() && !isShouldLogInFile() && !isShouldLogInDataBase()) {
            throw new Exception("Invalid configuration");
        }
    }

    private static boolean isShouldLogInFile() {
        return JobLogger.shouldLogInFile;
    }

    private static boolean isShouldLogInConsole() {
        return JobLogger.shouldLogInConsole;
    }

    private static boolean isShouldLogInDataBase() {
        return JobLogger.shouldLogInDataBase;
    }

    private static void validateLevel(boolean isMessage, boolean isWarning, boolean isError) throws Exception {
        if ((!JobLogger.isError && !JobLogger.isMessage && !JobLogger.isWarning) || (!isError && !isMessage && !isWarning)) {
            throw new Exception("Error or Warning or Message must be specified");
        }
    }

    public static boolean isMessageEmpty(String messageText) {
        return messageText == null || messageText.length() == 0;
    }
}
