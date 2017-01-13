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

    /**
     * Este metodo permite configurar el nivel de log, dispositivo de salida
     * mediante configuracion.
     *
     * @param shouldLogInFile
     * @param shouldLogInConsole
     * @param shouldLogInDataBase
     * @param isMessage
     * @param isWarning
     * @param isError
     * @param configuration
     * @deprecated Debe ser reemplazado mediante el metodo estatico config.
     */
    @Deprecated
    public JobLogger(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase,
            boolean isMessage, boolean isWarning, boolean isError, Map configuration) {
        JobLoggerLevel level = JobLoggerLevel.getLoggerLevel(isMessage, isWarning, isError);
        List<JobLoggerOutput> outputs = JobLoggerOutput.getLoggerOutputs(shouldLogInFile, shouldLogInConsole, shouldLogInDataBase);
        config(level, configuration, outputs);
    }

    /**
     * Permite registrar un mensaje de acuerdo al nivel habilitado.
     *
     * @param messageText Mensaje a registrar
     * @param isMessage
     * @param isWarning
     * @param isError
     * @throws Exception
     * @deprecated Debe ser reemplazado por el metodo estatico logBasedOnLevel
     */
    @Deprecated
    public static void LogMessage(String messageText, boolean isMessage, boolean isWarning, boolean isError) throws Exception {
        JobLoggerLevel level;
        level = JobLoggerLevel.getLoggerLevel(isMessage, isWarning, isError);
        logBasedOnLevel(messageText, level);
    }

    /**
     * Configura la clase para definir el nivel de log, asi como agregar los
     * dispositivos de salida.
     *
     * @param loggerLevel nivel del log.
     * @param configuration Configuracion para los dispositivos de salida.
     * @param loggerOutputs dispositivos de salida
     */
    public static void config(JobLoggerLevel loggerLevel, Map configuration, List<JobLoggerOutput> loggerOutputs) {
        logger = Logger.getLogger(LOGGER_NAME);
        setConfiguration(configuration);

        setLoggerOutputs(loggerOutputs);
        validateLoggerOutputs();

        setLevel(loggerLevel);
        validateLoggerLevel();
    }

    /**
     * Registra el mensaje mediante el nivel del log
     *
     * @param messageText Mensaje
     * @param level Nivel de Log
     */
    public static void logBasedOnLevel(String messageText, JobLoggerLevel level) {
        if (isMessageNotEmpty(messageText)) {
            validateLevel(level);
            messageText = messageText.trim();
            String textToLog = formatTextLog(messageText, level);
            addLoggerOutputs();
            showLog(level, textToLog);
        }
    }

    private static boolean isMessageNotEmpty(String messageText) {
        return !(messageText == null || messageText.length() == 0);
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

    private static void validateLevel(JobLoggerLevel level) {
        if (level == null) {
            throw new RuntimeException("Error or Warning or Message must be specified");
        }
    }

    private static void showLog(JobLoggerLevel level, String messageText) {
        logger.log(level, messageText);
    }

    protected static String formatTextLog(String messageText, JobLoggerLevel level) {
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
        removeAndAddHandlder(output.getHandler());
    }

    private static void removeAndAddHandlder(Handler handler) {
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
