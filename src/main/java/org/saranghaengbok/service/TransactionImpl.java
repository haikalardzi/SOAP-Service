package org.saranghaengbok.service;

import java.sql.Statement;
import java.util.Objects;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import com.sun.net.httpserver.HttpExchange;

import org.saranghaengbok.core.DatabaseConnection;
import org.saranghaengbok.models.Log;

@WebService(endpointInterface = "org.saranghaengbok.service.Transaction")
public class TransactionImpl implements Transaction{
    
    @Resource
    public WebServiceContext context;

    private String apiKey;

    public void setAPIKey(){
        MessageContext mc = this.context.getMessageContext();
        HttpExchange httpExchange = (HttpExchange)mc.get("com.sun.xml.internal.ws.http.exchange");
        this.apiKey = httpExchange.getRequestHeaders().getFirst("X-API-Key");
    }

    public boolean validateAPIKeyREST(){
        setAPIKey();
        if (apiKey == null){
            return false;
        } else if (apiKey.equals("rest")){
            return true;
        } else {
            return false;
        }
    }

    public boolean validateAPIKeyPHP(){
        setAPIKey();
        if (apiKey == null){
            return false;
        } else if (apiKey.equals("php")){
            return true;
        } else {
            return false;
        }
    }

    public String logger(String desc, WebServiceContext context, String endpoint){
        try{
            MessageContext mc = context.getMessageContext();
            HttpExchange httpExchange = (HttpExchange)mc.get("com.sun.xml.internal.ws.http.exchange");
            InetSocketAddress address = httpExchange.getRemoteAddress();
            String ip = address.getAddress().toString().substring(1);
            Log log = new Log();
            String apiKey = httpExchange.getRequestHeaders().getFirst("X-API-Key");
            String description = apiKey + ": " + desc;
            log.logging(ip, endpoint, description);
            return desc;
        } catch (Exception e){
            e.printStackTrace();
            return "sad";
        }
    }
    
    @Override
    public String createTransaction(String buyer_username, String seller_username, String list_item_id, String list_quantity) {
        if (!validateAPIKeyPHP()){
            return "Invalid API Key";
        } 
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        System.out.println(buyer_username);
        System.out.println(seller_username);
        System.out.println(list_item_id);
        System.out.println(list_quantity);
        if (list_item_id.isEmpty() || Objects.isNull(list_item_id) || list_item_id.equals(" ") ||
            list_quantity.isEmpty() || Objects.isNull(list_quantity) || list_quantity.equals(" ") ||
            buyer_username.isEmpty() || Objects.isNull(buyer_username) || buyer_username.equals(" ") || 
            seller_username.isEmpty() || Objects.isNull(seller_username) || seller_username.equals(" ")){
            return "No item checked out";
        } else {
            try {
                Statement statement = connection.createStatement();
                String[] list_item = list_item_id.split(",");
                String[] quantities = list_quantity.split(",");
                String[] list_seller = seller_username.split(",");
                for (int i = 0; i < list_item.length; i++) {
                    String query = "INSERT INTO transaction (buyer_username, seller_username, item_id, quantity) VALUES ('"+ buyer_username +"','"+ list_seller[i] +"',"+ list_item[i]+","+ quantities[i] +")"; 
                    statement.executeUpdate(query);
                    System.out.printf("buyer_username: %s, seller_username:%s, item_id: %s, quantity: %s\n",buyer_username, seller_username, list_item[i], quantities[i]);
                }
                statement.close();
                connection.close();
                return logger("transaction success", this.context, "createTransaction");
            } catch (Exception e) {
                e.printStackTrace();
                return logger("transaction failed", this.context, "createTransaction");
            }
        }
    }
    
    @Override
    public String getAllTransaction(int page) {
        if (!validateAPIKeyREST()){
            return "Invalid API Key";
        }
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        try{
            Statement statement = connection.createStatement();
            page = (page-1)*10;
            System.out.println("page: "+ page);
            String query = "SELECT * FROM transaction LIMIT "+page + ", 10";
            ResultSet result = statement.executeQuery(query);
            Boolean hasResult = false;
            String message = "{\"data\": [";
            while (result.next()) {
                message +=   "{\"transaction_id\": " + result.getInt("transaction_id") +
                            ", \"buyer_username\": \"" + result.getString("buyer_username") +
                            "\", \"sellerusername\": \"" + result.getString("seller_username") + 
                            "\", \"item_id\": "        + result.getInt("item_id") + 
                            ", \"quantities\": "     + result.getInt("quantity") + "},";
                System.out.println("transaction_id " + result.getInt("transaction_id"));
                System.out.println("username1: "+ result.getString("buyer_username"));
                hasResult = true;
            }
            message = message.substring(0, message.length() - 1);
            message += "]}";
            if (!hasResult) {
                message = "{\"data\": []}";
            }
            
            result.close();
            statement.close();
            logger("transaction history sent", this.context, "getAllTransaction");
            return message;
        } catch (Exception e){
            e.printStackTrace();
            return logger("Unexpected error occur", this.context, "getAllTransaction");
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                return logger("Unexpected error occur", this.context, "getAllTransaction");
            }
        }
    }

    @Override
    public String getAllBuyerTransaction(String username, int page) {
        if (!validateAPIKeyREST()){
            return "Invalid API Key";
        } 
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();

        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM transaction as t join transaction_items as ti on t.transaction_id = ti.transaction_id WHERE buyer_username = '" + username + "' LIMIT" + (page-1)*10 + "10";
            ResultSet result = statement.executeQuery(query);
            Boolean hasResult = false;
            String message = "{\"data\": [";
            while (result.next()) {
                message += "{\"transaction_id\": " + result.getInt("transaction_id") +
                            "\"buyer_username\": " + result.getString("buyer_username") +
                            "\"sellerusername\": " + result.getString("seller_username") + 
                            "\"item_id\": "        + result.getInt("item_id");
                hasResult = true;
            }
            message = message.substring(0, message.length() - 1);
            message += "]}";
            if (!hasResult) {
                message = "{\"data\": []}";
            }
            result.close();
            statement.close();
            return logger("buyer transaction history sent", this.context, "getAllbuyerTransaction");
        } catch (Exception e) {
            return logger("Unexpected error occur", this.context, "getAllbuyerTransaction");
        } finally {

            try {
                connection.close();
            } catch (Exception e) {
                return logger("Unexpected error occur", this.context, "getAllbuyerTransaction");
            }
        }
    }

    @Override
    public String getAllSellerTransaction(String username, int page) {
        if (!validateAPIKeyREST()){
            return "Invalid API Key";
        } 
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM transaction as t join transaction_items as ti on t.transaction_id = ti.transaction_id WHERE seller_username = '" + username + "' LIMIT" + (page-1)*10 + "10";
            ResultSet result = statement.executeQuery(query);
            Boolean hasResult = false;
            String message = "{\"data\": [";
            while (result.next()) {
                message += "{\"transaction_id\": " + result.getInt("transaction_id") +
                            "\"buyer_username\": " + result.getString("buyer_username") +
                            "\"sellerusername\": " + result.getString("seller_username") + 
                            "\"item_id\": "        + result.getInt("item_id");
                hasResult = true;
            }
            message = message.substring(0, message.length() - 1);
            message += "]}";
            if (!hasResult) {
                message = "{\"data\": []}";
            }
            logger("seller transaction sent", this.context, "getAllSellerTransaction");
            result.close();
            statement.close();
            return message;
        } catch (Exception e){
            logger("Unexpected error occur", this.context, "getAllSellerTransaction");
            return "Unexpected error occur";
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                logger("Unexpected error occur", this.context, "getAllSellerTransaction");
                return "Unexpected error occur";
            }
        }
    }

}
