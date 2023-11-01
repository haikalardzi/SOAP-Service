package org.saranghaengbok.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.DOCUMENT, parameterStyle = ParameterStyle.WRAPPED)
public interface Transaction {
    @WebMethod
    public String createTransaction(
        @WebParam(name = "transaction_id", targetNamespace = "http://service.saranghaengbok.org/") int transaction_id,
        @WebParam(name = "buyer_username", targetNamespace = "http://service.saranghaengbok.org/") String buyer_username,
        @WebParam(name = "seller_username", targetNamespace = "http://service.saranghaengbok.org/") String seller_username,
        @WebParam(name = "item_id", targetNamespace = "http://service.saranghaengbok.org/") List<Integer> list_item_id,
        @WebParam(name = "quantity", targetNamespace = "http://service.saranghaengbok.org/") List<Integer> list_quantity
    );

    @WebMethod
    public String getAllTransaction(
        @WebParam(name = "page", targetNamespace = "http://service.saranghaengbok.org/") int page
    );
    
    @WebMethod
    public String getAllBuyerTransaction(
        @WebParam(name = "username", targetNamespace = "http://service.saranghaengbok.org/") String username,
        @WebParam(name = "page", targetNamespace = "http://service.saranghaengbok.org/") int page
    );

    @WebMethod
    public String getAllSellerTransaction(
        @WebParam(name = "username", targetNamespace = "http://service.saranghaengbok.org/") String username,
        @WebParam(name = "page", targetNamespace = "http://service.saranghaengbok.org/") int page
    );


}
