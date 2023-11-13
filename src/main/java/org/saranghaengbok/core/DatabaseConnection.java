package org.saranghaengbok.core;

import java.sql.*;

public class DatabaseConnection {
    protected Connection conn;
    private String db_url;
    private String db_username;
    private String db_password;

    public DatabaseConnection(){
        db_url = "jdbc:mysql://db-soap-service:3306/saranghaengbok_soap";
        db_username = "root";
        db_password = "admin";
        try{
            this.conn = DriverManager.getConnection(db_url, db_username, db_password);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.conn;
    }
}
