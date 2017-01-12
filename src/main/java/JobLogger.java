/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLogger {

    private static boolean isMessage;
    private static boolean isWarning;
    private static boolean isError;
    private boolean initialized;
    private static Map configuration;
    private static Logger logger;

    public JobLogger(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase,
            boolean isMessage, boolean isWarning, boolean isError, Map configuration) {
        createLogger();

        JobLogger.isError = isError;
        JobLogger.isMessage = isMessage;
        JobLogger.isWarning = isWarning;

        setConfiguration(configuration);
        setHandlers(shouldLogInFile, shouldLogInConsole, shouldLogInDataBase);
    }

    private static void setConfiguration(Map configuration) {
        JobLogger.configuration = configuration;
    }

    private static void setHandlers(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase) {
        try {

            if (shouldLogInConsole) {
                addHandlder(new ConsoleHandler());
            }
            if (shouldLogInFile) {
                addHandlder(new JobFileHandler(configuration));
            }
            if (shouldLogInDataBase) {
                addHandlder(new JobDataBaseHandler(configuration));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while set Handlers", e);
        }
    }

    public static void createLogger() {
        logger = Logger.getLogger("MyLog");
    }

    public static void addHandlder(Handler handler) {
        logger.addHandler(handler);
    }

    public static void logBasedOnLevel(String messageText, boolean isMessage, boolean isWarning, boolean isError) throws Exception {
        //public static void logBasedOnLevel(String messageText, JobLoggerLevel level) throws Exception {
        if (isMessageEmpty(messageText)) {
            return;
        }
        validateHandlers();
        validateLevel(isMessage, isWarning, isError);

        messageText = messageText.trim();

        String textToLog = formatTextLog(messageText, isMessage, isWarning, isError);

        showLog(null, textToLog);
    }

    public static void showLog(JobLoggerLevel level, String messageText) {
        logger.log(Level.INFO, messageText);
    }

    public static String formatTextLog(String messageText, boolean isMessage, boolean isWarning, boolean isError) {
        String textToLog = "";
        if (isError && JobLogger.isError) {
            textToLog = textToLog + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        } else if (isWarning && JobLogger.isWarning) {
            textToLog = textToLog + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        } else if (isMessage && JobLogger.isMessage) {
            textToLog = textToLog + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }
        return textToLog;
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

    private static void validateHandlers() throws Exception {
        Handler[] handlers = logger.getHandlers();
        if (handlers == null || handlers.length == 0) {
            throw new Exception("Invalid configuration");
        }
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
