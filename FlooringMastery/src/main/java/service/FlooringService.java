/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.FlooringDaoException;
import dto.Order;
import dto.Product;
import dto.TaxRate;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author daler
 */
public interface FlooringService {
    
    Order createOrder(Order order) throws FlooringDaoException, InvalidDataException, NumberFormatException;
    
    List<Order> getOrdersByDate(String date) throws FlooringDaoException;
    
    Order getOrderToEdit(String date, int orderNumber) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException;
    
    Order editOrder(Order editedOrder, String date, int orderNumber) throws FlooringDaoException, InvalidDataException, NumberFormatException, InvalidOrderNumberException;
    
    Order removeOrder(String date, int orderNumber, String confirm) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException, InvalidDataException;
    
}
