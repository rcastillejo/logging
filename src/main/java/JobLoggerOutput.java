
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo
 */
public enum JobLoggerOutput {

    CONSOLE(new JobConsoleOutput()),
    FILE(new JobFileOutput()),
    DB(new JobDataBaseHandler());

    public static List<JobLoggerOutput> getLoggerOutputs(boolean shouldLogInFile, boolean shouldLogInConsole, boolean shouldLogInDataBase) {
        List<JobLoggerOutput> outputs = new ArrayList<JobLoggerOutput>();
        if (shouldLogInFile) {
            outputs.add(JobLoggerOutput.FILE);
        }
        if (shouldLogInConsole) {
            outputs.add(JobLoggerOutput.CONSOLE);
        }
        if (shouldLogInDataBase) {
            outputs.add(JobLoggerOutput.DB);
        }
        return outputs;
    }

    private final JobOutput output;

    private JobLoggerOutput(JobOutput output) {
        this.output = output;
    }

    public JobOutput getOutput() {
        return output;
    }
}
