/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Order;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import service.InvalidOrderNumberException;

/**
 *
 * @author daler
 */
public class OrderDaoFileImpl implements OrderDao {

    public static final String DELIMITER = ",";

    public List<Order> orders = new ArrayList<>();
    
    public String getMode() throws FlooringDaoException {
        Scanner sc;

        try {
            sc = new Scanner(new BufferedReader(new FileReader("mode.txt")));
        } catch (FileNotFoundException e) {
            throw new FlooringDaoException("Could not load order data into memory.", e);
        }
        String mode = sc.nextLine();
        sc.close();
        return mode;
    }

    public String ordersFileCreator() {
        String todaysDate = LocalDate.now().toString();
        return "ORDERS_" + todaysDate + ".txt";
    }
    
    public int orderNumberGenerator(){
        if (orders.isEmpty()){
            return 1;
        } else {
            return orders.stream()
                    .mapToInt(o -> o.getOrderNumber())
                    .max().getAsInt()+1;
        }
    }

    @Override
    public Order createOrder(Order order) throws FlooringDaoException {
        try {
            loadOrders();
        } catch (FlooringDaoException e) {
        }
        int orderNumber = orderNumberGenerator();
        order.setOrderNumber(orderNumber);
        orders.add(order);
        writeOrders();
        return order;
    }

    @Override
    public List<Order> getOrdersByDate(String date) throws FlooringDaoException {
        String fileToRead = "ORDERS_"+date+".txt";
        return loadOrdersByDate(fileToRead);
    }

    @Override
    public Order getOrderToEdit(String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException {
        String fileToRead = "ORDERS_"+date+".txt";
        orders = loadOrdersByDate(fileToRead);
        Order orderToEdit = null;
        for (Order o: orders) {
            if (o.getOrderNumber()==orderNumber) {
                orderToEdit = o;
                break;
            }
        }
        return orderToEdit;
    }
    
    @Override
    public Order editOrder(Order editedOrderToPersist, String date, int orderNumber) throws FlooringDaoException, InvalidOrderNumberException, NumberFormatException {
        String fileToRead = "ORDERS_"+date+".txt";
        orders = loadOrdersByDate(fileToRead);
        for (Order o: orders) {
            if (o.getOrderNumber()==orderNumber) {
                orders.remove(o);
                orders.add(editedOrderToPersist);
                break;
            }
        }
        writeEditedOrders(fileToRead);
        return null;
    }

    @Override
    public Order removeOrder(String date, int orderNumber) throws FlooringDaoException, NumberFormatException, InvalidOrderNumberException {
        String fileToRead = "ORDERS_"+date+".txt";
        orders = loadOrdersByDate(fileToRead);
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
        writeEditedOrders(fileToRead);
        return orderToDelete;
    }

    public Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);
        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(Integer.parseInt(orderTokens[0]));
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        BigDecimal taxRateBD = new BigDecimal(orderTokens[3]);
        orderFromFile.setTaxRate(taxRateBD);
        orderFromFile.setProductType(orderTokens[4]);
        BigDecimal areaBD = new BigDecimal(orderTokens[5]);
        orderFromFile.setArea(areaBD);
        BigDecimal costPerSquareFootBD = new BigDecimal(orderTokens[6]);
        orderFromFile.setMaterialCostPerSquareFoot(costPerSquareFootBD);
        BigDecimal laborCostPerSquareFootBD = new BigDecimal(orderTokens[7]);
        orderFromFile.setLaborCostPerSquareFoot(laborCostPerSquareFootBD);
        BigDecimal materialCostBD = new BigDecimal(orderTokens[8]);
        orderFromFile.setMaterialCost(materialCostBD);
        BigDecimal laborCostBD = new BigDecimal(orderTokens[9]);
        orderFromFile.setLaborCost(laborCostBD);
        BigDecimal taxBD = new BigDecimal(orderTokens[10]);
        orderFromFile.setTax(taxBD);
        BigDecimal totalBD = new BigDecimal(orderTokens[11]);
        orderFromFile.setTotalCost(totalBD);
        return orderFromFile;
    }

    public void loadOrders() throws FlooringDaoException {
        orders = new ArrayList<>();
        
        Scanner sc;

        try {
            sc = new Scanner(new BufferedReader(new FileReader(ordersFileCreator())));
        } catch (FileNotFoundException e) {
            throw new FlooringDaoException("Could not load order data into memory.", e);
        }
        String currentLine;
        Order currentOrder;
        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.add(currentOrder);
        }
        sc.close();
    }
    
    public List<Order> loadOrdersByDate(String fileToRead) throws FlooringDaoException {
        orders = new ArrayList<>();
        
        Scanner sc;

        try {
            sc = new Scanner(new BufferedReader(new FileReader(fileToRead)));
        } catch (FileNotFoundException e) {
            throw new FlooringDaoException("Could not load order data into memory.", e);
        }
        String currentLine;
        Order currentOrder;
        while (sc.hasNextLine()) {
            currentLine = sc.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            orders.add(currentOrder);
        }
        sc.close();
        return orders;
    }

    //code to put in training or production mode
    public String marshallOrder(Order anOrder) {
        // We need to turn a Student object into a line of text for our file.
        // For example, we need an in memory object to end up like this:
        // 4321::Charles::Babbage::Java-September1842

        // It's not a complicated process. Just get out each property,
        // and concatenate with our DELIMITER as a kind of spacer. 
        // Start with the student id, since that's supposed to be first.
        String orderAsText = anOrder.getOrderNumber() + DELIMITER;

        // add the rest of the properties in the correct order:
        orderAsText += anOrder.getCustomerName() + DELIMITER;
        orderAsText += anOrder.getState() + DELIMITER;
        orderAsText += anOrder.getTaxRate() + DELIMITER;
        orderAsText += anOrder.getProductType() + DELIMITER;
        orderAsText += anOrder.getArea() + DELIMITER;
        orderAsText += anOrder.getMaterialCostPerSquareFoot() + DELIMITER;
        orderAsText += anOrder.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += anOrder.getMaterialCost() + DELIMITER;
        orderAsText += anOrder.getLaborCost() + DELIMITER;
        orderAsText += anOrder.getTax() + DELIMITER;
        orderAsText += anOrder.getTotalCost();
        return orderAsText;
    }

    public void writeOrders() throws FlooringDaoException {
        if (getMode().equals("training")){
            //do nothing
        } else {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ordersFileCreator()));
        } catch (IOException e) {
            throw new FlooringDaoException("Could not save order data.", e);
        }

        String orderAsText;

        for (Order currentOrder : orders) {
            // turn a Student into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Student object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
        }
    }
    
    public void writeEditedOrders(String fileToRead) throws FlooringDaoException {
        if (getMode().equals("training")){
            //do nothing
        } else {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(fileToRead));
        } catch (IOException e) {
            throw new FlooringDaoException("Could not save order data.", e);
        }

        String orderAsText;

        for (Order currentOrder : orders) {
            // turn a Student into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Student object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
        }
    }
    
}
