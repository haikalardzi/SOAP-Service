package org.saranghaengbok.models;

import java.sql.Statement;

import org.saranghaengbok.core.DatabaseConnection;

public class Log extends DatabaseConnection {
    public Log(){
        super();
    }

    public String logging(String ip, String endpoint, String desc){
        try {
            Statement statement = this.conn.createStatement();
            String query = "INSERT INTO log (ip, endpoint, description) VALUES ('" + ip + "', '" + endpoint + "', \"" + desc + "\")";
            statement.executeUpdate(query);
            System.out.println("IP: "+ip);
            System.out.println("Endpoint: " + endpoint);
            System.out.println("Desc: " + desc);

            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create log";
        }
        return "Successfully create log";
    }
}
