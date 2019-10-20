/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Order;
import java.util.List;
import service.InvalidDateInputException;
import service.InvalidOrderNumberException;

/**
 *
 * @author daler
 */
public interface OrderDao {
    
    Order createOrder(Order order) throws FlooringDaoException, NumberFormatException;
    
    List<Order> getOrdersByDate(String date) throws FlooringDaoException;
    
    Order getOrderToEdit(String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException;
    
    Order editOrder(Order editedOrderToPersist, String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException;
    
    Order removeOrder(String date, int orderNumber) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException;
    
}
