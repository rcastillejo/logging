/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

/**
 *
 * @author Ricardo
 */
public class JobFileOutput implements JobOutput {

    private static final String FILE_FOLDER_PARAM = "logFileFolder";
    private static final String FILE_NAME = "logFile.txt";

    private String pathname;
    private FileHandler fileHandler;

    public void config(Map configuration) {
        try {
            this.pathname = readPathname(configuration);
            verifyLogFile(pathname);
            this.fileHandler = new FileHandler(pathname);
        } catch (Exception e) {
            throw new RuntimeException("Error while configurate", e);
        }
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

    public String getPathname() {
        return pathname;
    }

    public Handler getHandler() {
        return fileHandler;
    }
}
