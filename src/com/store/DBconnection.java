package com.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBconnection {
    private Connection connection;
    private Statement stmt;

    public String createUserOracle(String s, String s1, String s2, String s3)
            throws SQLException, ClassNotFoundException {
        System.out.println("Creating user method ");
        String s4 = "";
        if (!s2.equals("")) {
            if (!s3.equals("")) {
                s4 = new String("CREATE USER \"" + s + "\" IDENTIFIED BY \""
                        + s1 + "\" DEFAULT TABLESPACE " + s2 + " QUOTA " + s3
                        + " ON " + s2);

            }
            else {
                s4 = new String("CREATE USER \"" + s + "\" IDENTIFIED BY \""
                        + s1 + "\" DEFAULT TABLESPACE " + s2
                        + " TEMPORARY TABLESPACE TEMP QUOTA UNLIMITED ON " + s2);
            }
        }
        else {
            s4 = new String("CREATE USER \"" + s + "\" IDENTIFIED BY \"" + s1
                    + "\"");
        }
        try {
            connection = this.init();
            System.out.println("The new user statement " + s4);
            stmt = connection.createStatement();
            System.out.println("The new user statement " + s4);
            stmt.execute(s4);
            stmt.close();
        } finally {
            if (null != connection)
                connection.close();
        }
        return s;
    }

    public String grantRightsOracle(String s) throws SQLException, ClassNotFoundException {
        String s1 = new String("GRANT connect , resource , dba to \"" + s + "\"");
        System.out.println(s1);
        try {
            connection = this.init();
            stmt = connection.createStatement();
            System.out.println("granting "+s+"with the connect priveleges");
            stmt.execute(s1);
            stmt.close();
        }
        finally {
            if(null != connection)
                this.close(connection);
        }
        return s;
    }

    public Connection init() throws SQLException, ClassNotFoundException {
        System.out.println("initialising");
        String userName = "emp_user";
        String password = "password";
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        Connection connection = DriverManager.getConnection(url,userName,password);
        return connection;
    }

    public void close(Connection connection) throws SQLException {
        if (!connection.isClosed())
            connection.close();
    }

    public String deleteUserOracle(String s) throws SQLException, ClassNotFoundException {
        String s1 = new String("DROP USER \"" + s + "\" CASCADE");
        try {
            connection = this.init();
            stmt = connection.createStatement();
            stmt.execute(s1);
            stmt.close();
        } finally {
            if (connection != null)
                this.close(connection);
        }
        return "SUCCESS";
    }

    public static void main(String args[]) {
        String s = "";
        DBconnection oracleOperations = new DBconnection();
        try {
            System.out.println("deleting user " + s);
            s=oracleOperations.deleteUserOracle("storeManager");
            System.out.println("creating user ");
            s = oracleOperations.createUserOracle("storeManager", "storeManager", "", "");
            System.out.println("created user is " + s);
            s=oracleOperations.grantRightsOracle("storeManager");
            System.out.println("granting rights " + s);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
