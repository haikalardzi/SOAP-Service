package org.saranghaengbok.core;

import java.sql.*;

public class DatabaseConnection {
    protected Connection conn;
    private String db_url;
    private String db_username;
    private String db_password;

    public DatabaseConnection(){
        db_url = "jdbc:mysql://localhost:3306/saranghaengbok_soap";
        db_username = "root";
        db_password = "mysql";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(db_url, db_username, db_password);
        } catch (Exception e){
            System.err.println("Error connecting to db");
        }
    }

    public Connection getConnection(){
        return this.conn;
    }
}
