/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

/**
 *
 * @author Ricardo
 */
public class JobConsoleOutput implements JobOutput {

    private ConsoleHandler handler;

    public void config(Map<String, String> configuration) {
        this.handler = new ConsoleHandler();
    }

    public Handler getHandler() {
        return this.handler;
    }

}
