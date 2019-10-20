/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dto.Order;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author daler
 */
public class FlooringView {

    UserIO io = new UserIOConsoleImpl();

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("==============================================");
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Quit");
        io.print("==============================================");

        return io.readInt("Please select from the above choices.", 1, 5);
    }

    public Order getNewOrderInfo() {
        String customerName = io.readString("Enter customer name:");
        String state = io.readString("Enter customer state:");
        String productType = io.readString("Enter flooring type:");
        BigDecimal area = io.readBigDecimal("Enter area of flooring desired in square feet.");
        Order currentOrder = new Order(customerName, state, productType, area);
        return currentOrder;
    }

    public String getOrdersDate() {
        return io.readString("Please enter the date for which you want to view/edit/delete orders in YYYY-MM-DD format.");
    }

    public int getOrderNumber() {
        return io.readInt("Please enter the order number you wish to edit or delete.");
    }

    public Order getEditedOrderInfo(Order originalOrder) {

        String customerName = io.readString("Enter customer name (" + originalOrder.getCustomerName() + ")");
        if (customerName.isEmpty() || customerName.isBlank()) {
            customerName = originalOrder.getCustomerName();
        }
        String state = io.readString("Enter state (" + originalOrder.getState() + ")");
        if (state.isEmpty() || state.isBlank()) {
            state = originalOrder.getState();
        }
        String productType = io.readString("Enter product type (" + originalOrder.getProductType() + ")");
        if (productType.isEmpty() || productType.isBlank()) {
            productType = originalOrder.getProductType();
        }

        BigDecimal area = originalOrder.getArea();
        try {
            area = io.readBigDecimal("Enter area of flooring desired in square feet (" + originalOrder.getArea() + ")");
        } catch (NumberFormatException ex) {
        }
        Order editedOrder = new Order(customerName, state, productType, area);
        editedOrder.setOrderNumber(originalOrder.getOrderNumber());

        return editedOrder;

    }

    public void displayOrderToEditOrDelete(List<Order> ordersByDate, int orderNumber) {
        for (Order currentOrder : ordersByDate) {
            if (currentOrder.getOrderNumber() == orderNumber) {
                io.print(currentOrder.getOrderNumber() + "|" + currentOrder.getCustomerName() + "|"
                        + currentOrder.getState() + "|" + currentOrder.getTaxRate() + "|" + currentOrder.getProductType()
                        + "|" + currentOrder.getArea() + "|" + currentOrder.getMaterialCostPerSquareFoot()
                        + "|" + currentOrder.getLaborCostPerSquareFoot() + "|" + currentOrder.getMaterialCost() + "|"
                        + currentOrder.getLaborCost() + "|" + currentOrder.getTax() + "|" + currentOrder.getTotalCost());
            }
        }
    }

    public String confirmDelete() {
        return io.readString("Are you sure you want to delete this order? (y/n)");
    }

    public void displayOrdersByDate(List<Order> ordersByDate) {
        for (Order currentOrder : ordersByDate) {
            io.print(currentOrder.getOrderNumber() + "|" + currentOrder.getCustomerName() + "|"
                    + currentOrder.getState() + "|" + currentOrder.getTaxRate() + "|" + currentOrder.getProductType()
                    + "|" + currentOrder.getArea() + "|" + currentOrder.getMaterialCostPerSquareFoot()
                    + "|" + currentOrder.getLaborCostPerSquareFoot() + "|" + currentOrder.getMaterialCost() + "|"
                    + currentOrder.getLaborCost() + "|" + currentOrder.getTax() + "|" + currentOrder.getTotalCost());
        }
    }

    public void displayError(String message) {
        io.print("ERROR: " + message);
    }

    public void unknownCommand() {
        io.print("UNKNOWN COMMAND");
    }

    public void exitMessage() {
        io.print("Goodbye!");
    }

}
