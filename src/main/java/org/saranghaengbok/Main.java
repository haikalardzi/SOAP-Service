package org.saranghaengbok;

import org.saranghaengbok.service.TransactionImpl;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        try{
            System.out.println("Server: http://localhost:8081/ws/transaction");
            Endpoint.publish("http://0.0.0.0:8081/ws/transaction", new TransactionImpl());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}