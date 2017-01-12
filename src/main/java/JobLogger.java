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
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class JobLogger {

    private static final String LOGGER_NAME = "MyLog";

    private static Logger logger;
    private static Map configuration;
    private static List<JobLoggerOutput> loggerOutputs;

    public JobLogger(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase,
            boolean isMessage, boolean isWarning, boolean isError, Map configuration) {
        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(isMessage, isWarning, isError);
        List<JobLoggerOutput> outputs = JobLoggerOutput.getLoggerOutputs(shouldLogInFile, shouldLogInConsole, shouldLogInDataBase);
        config(level, configuration, outputs);
    }

    public static void logBasedOnLevel(String messageText, boolean isMessage, boolean isWarning, boolean isError) throws Exception {
        JobLoggerLevel level;
        level = JobLoggerLevel.getLoggerLevel(isMessage, isWarning, isError);
        logBasedOnLevel(messageText, level);
    }

    public static void config(JobLoggerLevel loggerLevel, Map configuration, List<JobLoggerOutput> loggerOutputs) {
        logger = Logger.getLogger(LOGGER_NAME);
        setConfiguration(configuration);

        setLoggerOutputs(loggerOutputs);
        validateLoggerOutputs();

        setLevel(loggerLevel);
        validateLoggerLevel();
    }

    public static void logBasedOnLevel(String messageText, JobLoggerLevel level) throws Exception {
        if (isMessageEmpty(messageText)) {
            return;
        }
        validateLevel(level);
        messageText = messageText.trim();
        String textToLog = formatTextLog(messageText, level);
        addLoggerOutputs();
        showLog(level, textToLog);
    }

    private static boolean isMessageEmpty(String messageText) {
        return messageText == null || messageText.length() == 0;
    }

    private static void validateLoggerOutputs() {
        if (loggerOutputs == null || loggerOutputs.isEmpty()) {
            throw new RuntimeException("Invalid configuration");
        }
    }

    private static void validateLoggerLevel() {
        if (logger.getLevel() == null) {
            throw new RuntimeException("Error or Warning or Message must be specified");
        }
    }

    private static void validateLevel(JobLoggerLevel level) throws Exception {
        if (level == null) {
            throw new RuntimeException("Error or Warning or Message must be specified");
        }
    }

    private static void showLog(JobLoggerLevel level, String messageText) {
        logger.log(level, messageText);
    }

    public static String formatTextLog(String messageText, JobLoggerLevel level) {
        StringBuilder textToLog = new StringBuilder();
        textToLog.append(level.getName());
        textToLog.append(" ");
        textToLog.append(DateFormat.getDateInstance(DateFormat.LONG).format(new Date()));
        textToLog.append(messageText);
        return textToLog.toString();
    }

    public static void getAndSetLoggerLevel(boolean isMessage, boolean isWarning, boolean isError) {
        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(isMessage, isWarning, isError);
        setLevel(level);
    }

    private static void addLoggerOutputs() {
        for (JobLoggerOutput output : loggerOutputs) {
            addLoggerOutput(output);
        }
    }

    public static void addLoggerOutput(JobLoggerOutput jobOutput) {
        configAndAddOutput(jobOutput.getOutput());
    }

    protected static void configAndAddOutput(JobOutput output) {
        output.config(configuration);
        addHandlder(output.getHandler());
    }

    private static void addHandlder(Handler handler) {
        handler.setLevel(logger.getLevel());
        logger.removeHandler(handler);
        logger.addHandler(handler);
    }

    public static void setLevel(JobLoggerLevel level) {
        logger.setLevel(level);
    }

    private static void setConfiguration(Map configuration) {
        JobLogger.configuration = configuration;
    }

    public static void setLoggerOutputs(List<JobLoggerOutput> loggerOutputs) {
        JobLogger.loggerOutputs = loggerOutputs;
    }
}
