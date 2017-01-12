/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo
 */
public class JobLoggerLevel {

    public static final JobLoggerLevel MESSAGE = new JobLoggerLevel(1, "message");
    public static final JobLoggerLevel WARNING = new JobLoggerLevel(2, "warning");
    public static final JobLoggerLevel ERROR = new JobLoggerLevel(3, "error");

    private final int code;
    private final String name;

    public JobLoggerLevel(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.code;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JobLoggerLevel other = (JobLoggerLevel) obj;
        if (this.code != other.code) {
            return false;
        }
        return true;
    }

    
}
