/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.FlooringDaoException;
import dto.Order;
import java.util.ArrayList;
import java.util.List;
import ui.FlooringView;
import service.FlooringService;
import service.InvalidDataException;
import service.InvalidOrderNumberException;

/**
 *
 * @author daler
 */
public class FlooringController {

    FlooringView view;

    FlooringService service;

    public FlooringController(FlooringService service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean run = true;
        int menuSelection = 0;
        try {
            while (run) {
                menuSelection = getMenuSelection();
                switch (menuSelection) {
                    case 1:
                        getOrdersByDate();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        run = false;
                        break;
                    default:
                        view.unknownCommand();
                }
            }
        } catch (FlooringDaoException ex) {
            view.displayError(ex.getMessage());
        }
        view.exitMessage();
    }

    public int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    public void getOrdersByDate() throws FlooringDaoException { //validation good to go
        boolean validDate = true;
        do {
            try {
                String date = view.getOrdersDate();
                List<Order> ordersByDate = new ArrayList<>();
                ordersByDate = service.getOrdersByDate(date);
                validDate = true;
                view.displayOrdersByDate(ordersByDate);
            } catch (FlooringDaoException ex) {
                view.displayError("Orders file not found for given date. Please make sure your date is entered EXACTLY as YYYY-MM-DD or try a different date.");
                validDate = false;
            }
        } while (!validDate);
    }

    public void createOrder() throws FlooringDaoException { //validation should be good to go
        boolean validData = true;
        do {
            try {
                Order newOrder = view.getNewOrderInfo();
                service.createOrder(newOrder);
                validData = true;
            } catch (InvalidDataException ex) {
                view.displayError(ex.getMessage());
                validData = false;
            } catch (NumberFormatException ex) {
                view.displayError("Area must consist of digits 0-9 with or without a decimal point.");
                validData = false;
            }
        } while (!validData);
    }

    public void editOrder() throws FlooringDaoException { //validation should be good to go
        boolean validData = true;
        do {
            try {
                boolean validDateOrderNumber = true;
                do {
                    try {
                        String date = view.getOrdersDate();
                        int orderNumber = view.getOrderNumber();
                        Order originalOrder = service.getOrderToEdit(date, orderNumber);//validation happens here for edit method which is why i don't need it in the service edit method?
                        validDateOrderNumber = true;
                        service.editOrder(view.getEditedOrderInfo(originalOrder), date, orderNumber);
                        validDateOrderNumber = true;
                    } catch (FlooringDaoException ex) {
                        view.displayError("Orders file not found for given date. Please make sure your date is entered EXACTLY as YYYY-MM-DD or try a different date.");
                        validDateOrderNumber = false;
                    } catch (NumberFormatException ex) {
                        view.displayError("Invalid order number. Order number must consist of digits 0-9 only with no decimal points."); //issue here or below
                        validDateOrderNumber = false;
                    } catch (InvalidOrderNumberException ex) {
                        view.displayError(ex.getMessage());
                        validDateOrderNumber = false;
                    }
                } while (!validDateOrderNumber);
            } catch (InvalidDataException ex) {
                view.displayError(ex.getMessage());
                validData = false;
            } catch (NumberFormatException ex) {
                view.displayError("Area must consist of digits 0-9 with or without a decimal point.");//issue here or below
                validData = false;
            }
        } while (!validData);
    }

    public void removeOrder() throws FlooringDaoException { //validation mostly good to go except the comment below
        boolean validDateOrderNumber = true;
        do {
            try {
                String date = view.getOrdersDate();
                int orderNumber = view.getOrderNumber();
                List<Order> ordersByDate = new ArrayList<>();
                ordersByDate = service.getOrdersByDate(date);
                view.displayOrderToEditOrDelete(ordersByDate, orderNumber);
                validDateOrderNumber = true;
                boolean confirmValid = true;
                do {
                    try {
                        String confirm = view.confirmDelete(); //unfortunately this line has to execute for it to pick up an invalidOrder exception. not the best but serviceable
                        service.removeOrder(date, orderNumber, confirm);
                        confirmValid = true;
                    } catch (InvalidDataException ex) {
                        view.displayError(ex.getMessage());
                        confirmValid = false;
                    }
                } while (!confirmValid);
                validDateOrderNumber = true;

            } catch (FlooringDaoException ex) {
                view.displayError("Orders file not found for given date. Please make sure your date is entered EXACTLY as YYYY-MM-DD or try a different date.");
                validDateOrderNumber = false;
            } catch (NumberFormatException ex) {
                view.displayError("Invalid order number. Order number must consist of digits 0-9 only with no decimal points.");
                validDateOrderNumber = false;
            } catch (InvalidOrderNumberException ex) {
                view.displayError(ex.getMessage());
                validDateOrderNumber = false;
            }
        } while (!validDateOrderNumber);
    }

}
