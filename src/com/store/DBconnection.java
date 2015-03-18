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

    public String deleteTable(String TABLE_NAME) throws SQLException, ClassNotFoundException {
        String s1 = new String("DROP TABLE \"" + TABLE_NAME + "\"");
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

    private String createTables() throws SQLException, ClassNotFoundException {
        connection = this.init();
        String categoriesQuery = "CREATE TABLE CATEGORY (ID NUMBER(5) NOT NULL," +
                " NAME VARCHAR(100)," +
                " PRIMARY KEY(ID))";
        String bookCreateQuery = "CREATE TABLE BOOK (" +
                "ISBN NUMBER(13) NOT NULL, " +
                "TITLE VARCHAR(100), " +
                "EDITION VARCHAR(100), " +
                "AUTHOR VARCHAR(100), " +
                "PUBLISHER VARCHAR(100), " +
                "PRICE NUMBER(4), " +
                "PRIMARY KEY(ISBN)," +
                " CATEGORY_ID NUMBER(5) REFERENCES CATEGORY(ID))";
        String stockQuery = "CREATE TABLE STOCK (" +
                "BOOK_ISBN NUMBER(13) REFERENCES BOOK(ISBN)," +
                " UNITS NUMBER(4))";
        String cashierQuery = "CREATE TABLE CASHIER (" +
                "ID NUMBER(2) NOT NULL," +
                " NAME VARCHAR(30)," +
                " PRIMARY KEY(ID))";
        String billQuery = "CREATE TABLE BILL (" +
                "ID NUMBER(6) NOT NULL," +
//                " DATE DATE(0)," +
                " TIME TIMESTAMP," +
                " CUSTOMER VARCHAR(30)," +
                " TOTAL_COST NUMBER(6),"+
                " CASHIER_ID NUMBER(2) REFERENCES CASHIER(ID)," +
                " PRIMARY KEY(ID))";
        System.out.println(categoriesQuery);
        System.out.println(bookCreateQuery);
        System.out.println(stockQuery);
        System.out.println(cashierQuery);
        System.out.println(billQuery);

        try {
            stmt = connection.createStatement();
            stmt.addBatch(categoriesQuery);
            stmt.addBatch(bookCreateQuery);
            stmt.addBatch(stockQuery);
            stmt.addBatch(cashierQuery);
            stmt.addBatch(billQuery);
            stmt.executeBatch();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (stmt != null)
                    connection.close();
            } catch (SQLException se) {
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "SUCCESS";
    }

    public static void main(String args[]) {
        String s = "";
        DBconnection oracleOperations = new DBconnection();
        try {
            System.out.println("deleting user " + s);
            s = oracleOperations.deleteUserOracle("storeManager");
            System.out.println("creating user ");
            s = oracleOperations.createUserOracle("storeManager", "storeManager", "", "");
            System.out.println("created user is " + s);
            s=oracleOperations.grantRightsOracle("storeManager");
            System.out.println("granting rights " + s);
//            s=oracleOperations.deleteTable("STOCK");
//            System.out.println("deleted table category");
//            s=oracleOperations.deleteTable("BOOK");
//            System.out.println("deleted table book");
//            s=oracleOperations.deleteTable("CATEGORY");
//            System.out.println("deleted table category");
//            s=oracleOperations.deleteTable("BILL");
//            System.out.println("deleted table bill");
//            s=oracleOperations.deleteTable("CASHIER");
//            System.out.println("deleted table cashier");
            s=oracleOperations.createTables();
            System.out.println("table Book created "+ s);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}