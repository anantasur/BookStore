package com.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBconnection {

    public static final String isSUCCESS = "SUCCESS";

    public static final String isORACLE = "ORACLE";

    public static final String isFail = "ERROR";

    public String dbDriver;

    public String dbURL;

    public String dbUserName;

    public String dbName;

    public String dbPassword;

    public String dbType;

    private Connection con;

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
            System.out.println(s4);
        }
        try {
            con = this.init();
            System.out.println("The new user statemeent " + s4);
            stmt = con.createStatement();
            System.out.println("The new user statemeent " + s4);
            stmt.execute(s4);


            System.out.println("the info success "+stmt.SUCCESS_NO_INFO);

            stmt.close();
        } finally {
            if (null != con) {
                con.close();
            }
        }
        return "SUCCESS";
    }

    public String grantRightsOracle(String s)
            throws SQLException, ClassNotFoundException
    {

        String s1 = new String("GRANT connect , resource , dba to \"" + s + "\"");
        System.out.println(s1);
        try
        {
            con=this.init();
            stmt = con.createStatement();
            System.out.println("granting "+s+"with the connect priveleges");
            stmt.execute(s1);
            stmt.close();
        }
        finally
        {
            if(null != con)
            {
                this.close(con);
            }
        }
        return "SUCCESS";
    }

    public Connection init() throws SQLException, ClassNotFoundException {
        System.out.println("initialising");
        String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);
        String nameForConnect = "emp_user";
        String password = "password";
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        Properties p = new Properties();
        p.put("user",nameForConnect);
        p.put("password",password);
//        p.put("internal_logon","sysdba");
        Connection con = DriverManager.getConnection(url,p);
        return  con;
    }

    public void close(Connection connection) throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }
    public String disableUserOracle(String s) throws SQLException,
            ClassNotFoundException {
        String s1 = "REVOKE CONNECT, RESOURCE, SELECT ANY TABLE FROM \"" + s
                + "\"";
        try {
            con = this.init();
            stmt = con.createStatement();
            stmt.execute(s1);
            stmt.close();
        } finally {
            if (null != con) {
                this.close(con);
            }
        }
        return "SUCCESS";
    }

    public String deleteUserOracle(String s) throws SQLException,
            ClassNotFoundException {
        String s1 = new String("DROP USER \"" + s + "\" CASCADE");
        try {
            con = this.init();
            stmt = con.createStatement();
            stmt.execute(s1);
            stmt.close();
        } finally {
            if (null != con) {
                this.close(con);
            }
        }
        return "SUCCESS";
    }

    public static void main(String args[]) {
        String s = "";

        DBconnection oracleOperations = new DBconnection();
        try {
            s=oracleOperations.deleteUserOracle("storeManager");
            System.out.println("deleting user " + s);
            System.out.println("creating user");
            s = oracleOperations.createUserOracle("storeManager", "storeManager", "", "");
            System.out.println("creating user is " + s);
            s=oracleOperations.grantRightsOracle("storeManager");
            System.out.println("granting rights " + s);


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
