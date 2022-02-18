package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {


    private ConnectionUtil(){

    }


    public static Connection getConnection() throws SQLException {

        Connection con;

        try{
            String dbUrl = "jdbc:postgresql://34.71.200.147:5432/postgres";
            con = DriverManager.getConnection(dbUrl, ("postgres"), ("password"));
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}