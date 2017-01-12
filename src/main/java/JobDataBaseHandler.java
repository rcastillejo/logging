/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo
 */
public class JobDataBaseHandler extends Handler {

    private final Properties connectionProperties;
    private final String dbms;
    private final String serverName;
    private final String portNumber;

    public JobDataBaseHandler(Map<String, String> configuration) {
        connectionProperties = new Properties();
        connectionProperties.put("user", configuration.get("userName"));
        connectionProperties.put("password", configuration.get("password"));

        dbms = configuration.get("dbms");
        serverName = configuration.get("serverName");
        portNumber = configuration.get("portNumber");
    }

    @Override
    public void publish(LogRecord record) {
        Connection connection = null;

        try {
            connection = getConnection();

            Statement statement = createStatement(connection);

            String sqlCommand = createSqlCommand(record);

            executeStatement(statement, sqlCommand);
        } catch (SQLException ex) {
            Logger.getLogger(JobDataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public void flush() {
        System.out.println("flushing!");
    }

    @Override
    public void close() throws SecurityException {
        System.out.println("closing!");
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName
                + ":" + portNumber + "/", connectionProperties);
        return connection;
    }

    protected Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    protected String createSqlCommand(LogRecord logRecord) {
        Level level = logRecord.getLevel();
        return "insert into Log_Values('" + logRecord.getMessage() + "', " + String.valueOf(level.intValue()) + ")";
    }

    protected int executeStatement(Statement statement, String sqlCommand) throws SQLException {
        return statement.executeUpdate(sqlCommand);
    }

    protected void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                Logger.getLogger(JobLogger.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }
}
