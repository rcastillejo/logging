
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public class DummyOutput extends Handler implements JobOutput {

    private String message; 

    public void config(Map<String, String> configuration) { 
    } 

    @Override
    public void publish(LogRecord record) {
        message = record.getMessage();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public String getMessage() {
        return message;
    }

    public Handler getHandler() {
        return this;
    }

}
