/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import service.InvalidOrderNumberException;

/**
 *
 * @author daler
 */
public class FlooringOrderDaoStubImpl implements OrderDao {
    
    private Order onlyOrder;
    private List<Order> orders = new ArrayList<>();
    
    public FlooringOrderDaoStubImpl() {
        onlyOrder = new Order();
        onlyOrder.setOrderNumber(1);
        onlyOrder.setCustomerName("testName");
        onlyOrder.setState("OH");
        onlyOrder.setProductType("carpet");
        BigDecimal testArea = new BigDecimal("100");
        onlyOrder.setArea(testArea);
    }

    @Override
    public Order createOrder(Order order) throws FlooringDaoException, NumberFormatException {
        return order;
    }

    @Override
    public List<Order> getOrdersByDate(String date) throws FlooringDaoException {
        //this is passthrough in the service so it shouldn't need anything here
        return orders;
    }

    @Override
    public Order getOrderToEdit(String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException {
        orders.add(onlyOrder);
        for (Order o : orders) {
            if (o.getOrderNumber()==orderNumber) {
                return o;
            }
        }
        return null;
    }

    @Override
    public Order editOrder(Order editedOrderToPersist, String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException {

        for (Order o: orders) {
            if (o.getOrderNumber()==orderNumber) {
                orders.remove(o);
                orders.add(editedOrderToPersist);
                break;
            }
        }
        return editedOrderToPersist;
    }

    @Override
    public Order removeOrder(String date, int orderNumber) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException {
        orders.add(onlyOrder);
        Order orderToDelete = null;
        for (Order o : orders){
            if (o.getOrderNumber()==orderNumber){
                orderToDelete = o;
                break;
            }
        }
        if (orderToDelete!=null){
            orders.remove(orderToDelete);
        }
        return orderToDelete;
    }
    
}
