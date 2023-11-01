package org.saranghaengbok;

import org.saranghaengbok.service.TransactionImpl;

import javax.xml.ws.Endpoint;

public class App {
    public static void main(String[] args) {
        try{
            System.out.println("Server: http://localhost:8080/ws/transaction");
            Endpoint.publish("http://localhost:8080/ws/transaction", new TransactionImpl());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}