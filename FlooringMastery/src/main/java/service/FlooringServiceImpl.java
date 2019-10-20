/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.FlooringDaoException;
import dao.OrderDao;
import dao.ProductDao;
import dao.TaxDao;
import dto.Order;
import dto.Product;
import dto.TaxRate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daler
 */
public class FlooringServiceImpl implements FlooringService {

    OrderDao orderDao;
    ProductDao productDao;
    TaxDao taxDao;

    public FlooringServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    @Override
    public Order createOrder(Order newOrder) throws FlooringDaoException, InvalidDataException, NumberFormatException {
        Order filledOrder = fillOutOrder(newOrder);
        orderDao.createOrder(filledOrder);
        return filledOrder;
    }

    @Override
    public List<Order> getOrdersByDate(String date) throws FlooringDaoException {
        return orderDao.getOrdersByDate(date); //invalid date exception is caught in the controller so I guess this is pass through?
    }

    @Override
    public Order getOrderToEdit(String date, int orderNumber) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException {
        Order orderToEdit = orderDao.getOrderToEdit(date, orderNumber);
        if (orderToEdit == null) {
            throw new InvalidOrderNumberException("Order number not found. Please try a different order number.");
        }
        return orderToEdit;
    }

    @Override
    public Order editOrder(Order editedOrder, String date, int orderNumber) throws FlooringDaoException, InvalidDataException, NumberFormatException, InvalidOrderNumberException {
        Order editedOrderToPersist = new Order();
        editedOrderToPersist.setOrderNumber(editedOrder.getOrderNumber());
        orderNumber = editedOrderToPersist.getOrderNumber(); //probably don't need this line
        editedOrderToPersist = fillOutOrder(editedOrder);
        orderDao.editOrder(editedOrderToPersist, date, orderNumber);
        return editedOrderToPersist;
    }

    @Override
    public Order removeOrder(String date, int orderNumber, String confirm) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException, InvalidDataException {
        Order orderToDelete = null;
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("n")) {
            //do nothing
            if (confirm.equalsIgnoreCase("y")) {
                
                orderToDelete = orderDao.removeOrder(date, orderNumber);
                if (orderToDelete == null) {
                    throw new InvalidOrderNumberException("Order number not found. Please try a different order number.");
                }
            }
        } else {
            throw new InvalidDataException("Invalid confirmation. Please enter either 'y' or 'n' with no spaces or extra characters.");
        }
        return orderToDelete;
    }
    
    public Order fillOutOrder(Order orderToFill) throws InvalidDataException, FlooringDaoException, NumberFormatException {
        if (orderToFill.getCustomerName().isBlank() || orderToFill.getCustomerName().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty.");
        }
        String state = null;
        List<TaxRate> states = taxDao.getAllTaxRates();
        for (TaxRate t : states){
            if (t.getState().equalsIgnoreCase(orderToFill.getState())){
                state = t.getState();
            }
        }
        if (state == null) {
            throw new InvalidDataException("Invalid state name.");
        }
        BigDecimal taxRatePercent = taxDao.getTaxRate(state);
        orderToFill.setTaxRate(taxRatePercent);
        String productName = null;
        List<Product> products = productDao.getAllProducts();
        for (Product p : products){
            if (p.getName().equalsIgnoreCase(orderToFill.getProductType())){
                productName = p.getName();
            }
        }
        if (productName == null) {
            throw new InvalidDataException("Invalid product name.");
        }
        orderToFill.setMaterialCostPerSquareFoot(productDao.getProduct(productName).getCostPerSquareFoot());//shouldn't need validation
        orderToFill.setLaborCostPerSquareFoot(productDao.getProduct(productName).getLaborCostPerSquareFoot());//shouldn't need validation
        orderToFill.setMaterialCost(orderToFill.getMaterialCostPerSquareFoot().multiply(orderToFill.getArea()));//shouldn't need validation
        orderToFill.setLaborCost(orderToFill.getLaborCostPerSquareFoot().multiply(orderToFill.getArea()));//shouldn't need validation
        BigDecimal oneHundred = new BigDecimal("100");
        BigDecimal taxRateDecimal = taxRatePercent.divide(oneHundred);
        orderToFill.setTax(taxRateDecimal.multiply(orderToFill.getLaborCost().add(orderToFill.getMaterialCost())).setScale(2, RoundingMode.HALF_UP));
        orderToFill.setTotalCost(orderToFill.getLaborCost().add(orderToFill.getMaterialCost().add(orderToFill.getTax())));
        return orderToFill;
    }

}
