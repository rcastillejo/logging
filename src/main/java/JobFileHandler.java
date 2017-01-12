/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo
 */
public class JobFileHandler extends Handler {

    private static final String FILE_FOLDER_PARAM = "logFileFolder";
    private static final String FILE_NAME = "logFile.txt";

    private final String pathname;
    private final FileHandler fileHandler;

    public JobFileHandler(Map configuration) throws IOException  {
        this.pathname = readPathname(configuration);
        verifyLogFile(pathname);
        this.fileHandler = new FileHandler(pathname);
    }

    private String readPathname(Map configuration) {
        return configuration.get(FILE_FOLDER_PARAM) + "/" + FILE_NAME;
    }

    private void verifyLogFile(String pathname) throws IOException {
        File logFile = new File(pathname);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }

    @Override
    public void publish(LogRecord record) {
        fileHandler.publish(record);
    }

    @Override
    public void flush() {
        fileHandler.flush();
    }

    @Override
    public void close() throws SecurityException {
        fileHandler.close();
    }

    public String getPathname() {
        return pathname;
    }

}
