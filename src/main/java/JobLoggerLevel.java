
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public class JobLoggerLevel extends Level {

    public static final JobLoggerLevel MESSAGE = new JobLoggerLevel("message", 1);
    public static final JobLoggerLevel WARNING = new JobLoggerLevel("warning", 2);
    public static final JobLoggerLevel ERROR = new JobLoggerLevel("error", 3);

    public JobLoggerLevel(String name, int value) {
        super(name, value);
    }

    public int getCode() {
        return intValue();
    }

}
