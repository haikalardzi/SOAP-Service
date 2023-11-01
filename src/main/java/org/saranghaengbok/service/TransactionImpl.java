package org.saranghaengbok.service;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
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
        MessageContext messageContext = this.context.getMessageContext();
        HttpExchange exchange = (HttpExchange) messageContext.get("com.sun.xml.ws.http.exchange");
        this.apiKey = exchange.getRequestHeaders().getFirst("API-Key");
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
    
    @Override
    public String createTransaction(int transaction_id, String buyer_username, String seller_username, List<Integer> list_item_id, List<Integer> list_quantity) {
        if (!validateAPIKeyPHP()){
            return "Invalid API Key";
        } 
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        if ((list_item_id.isEmpty() || Objects.isNull(list_item_id)) &&
            list_quantity.isEmpty() || Objects.isNull(list_quantity)){
            return "No item checked out";
        } else {
            try {
                Statement statement = connection.createStatement();
                String query = "INSERT INTO transaction (transaction_id, buyer_username, seller_username) VALUES ("+ transaction_id +", '"+ buyer_username + "', '" + seller_username +"')"; 
                statement.executeQuery(query);
                for (int i = 0; i < list_item_id.size(); i++) {
                    String query2 = "INSERT INTO transaction_item (transaction_id, item_id, quantity) VALUES ("+ transaction_id +","+ list_item_id.get(i)+","+ list_quantity.get(i) +")"; 
                    statement.executeQuery(query2);
                    System.out.printf("transaction_item: &d, item_id: %d, quantity: %d", transaction_id, list_item_id.get(i), list_quantity.get(i));
                }
                Log.logger("transaction success", this.apiKey, this.context);
                statement.close();
                return "transaction success";
            } catch (Exception e) {
                e.printStackTrace();
                Log.logger("transaction failed", this.apiKey, this.context);
                return "transaction failed";
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    Log.logger("Unexpected error", this.apiKey, this.context);
                    return "Unexpected error";
                }
            }
        }
    }
    
    @Override
    public String getAllTransaction(int page) {
        if (!validateAPIKeyPHP()){
            return "Invalid API Key";
        } 
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM transaction as t join transaction_items as ti on t.transaction_id = ti.transaction_id LIMIT" + (page-1)*10 + "10";
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
            
            Log.logger(message, this.apiKey, this.context);
            result.close();
            statement.close();
            return message;
        } catch (Exception e){
            Log.logger("Unexpected error occur", this.apiKey, this.context);
            return "Unexpected error occur";
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                Log.logger("Unexpected error occur", this.apiKey, this.context);
                return "Unexpected error occur";
            }
        }
    }

    @Override
    public String getAllBuyerTransaction(String username, int page) {
        if (!validateAPIKeyPHP()){
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
            Log.logger(message, this.apiKey, this.context);
            result.close();
            statement.close();
            return message;
        } catch (Exception e) {
            Log.logger("Unexpected error occur", this.apiKey, this.context);
            return "Unexpected error occur";
        } finally {

            try {
                connection.close();
            } catch (Exception e) {
                Log.logger("Unexpected error occur", this.apiKey, this.context);
                return "Unexpected error occur";
            }
        }
    }

    @Override
    public String getAllSellerTransaction(String username, int page) {
        if (!validateAPIKeyPHP()){
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
            Log.logger(message, this.apiKey, this.context);
            result.close();
            statement.close();
            return message;
        } catch (Exception e){
            Log.logger("Unexpected error occur", this.apiKey, this.context);
            return "Unexpected error occur";
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                Log.logger("Unexpected error occur", this.apiKey, this.context);
                return "Unexpected error occur";
            }
        }
    }

}
