
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DummyDataBaseHandler extends JobDataBaseHandler {

    private String sqlCommand;

    @Override
    protected void closeConnection(Connection connection) {

    }

    @Override
    protected Statement createStatement(Connection connection) throws SQLException {
        return null;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    protected int executeStatement(Statement statement, String sqlCommand) throws SQLException {
        System.out.println("ejecutando comando [" + sqlCommand + "]");
        return -1;
    }

    @Override
    protected String createSqlCommand(LogRecord logRecord) {
        this.sqlCommand = super.createSqlCommand(logRecord);
        return this.sqlCommand;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

}
