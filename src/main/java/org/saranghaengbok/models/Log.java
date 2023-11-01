package org.saranghaengbok.models;

import java.sql.Statement;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;

import org.saranghaengbok.core.DatabaseConnection;

public class Log extends DatabaseConnection {
    public Log(){
        super();
    }

    public String logging(String ip, String endpoint, String desc){
        try {
            Statement statement = this.conn.createStatement();
            String query = "INSERT INTO log (ip, endpoint, desc) VALUES ('" + ip + "', '" + endpoint + "', '" + desc + "')";
            statement.executeUpdate(query);

            statement.close();
            conn.close();
        } catch (Exception e) {
            return "Failed to create log";
        }
        

        return "Successfully create log";
    }

    
    public static void logger(String desc, String apiKey, WebServiceContext context){
        MessageContext messageContext = context.getMessageContext();
        HttpExchange exchange = (HttpExchange) messageContext.get("com.sun.xml.ws.http.exchange");
        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
        String endpoint = exchange.getRequestURI().toString();
        Log log = new Log();
        String description = apiKey + ": " + desc;
        log.logging(ip, endpoint, description);
    }
}
