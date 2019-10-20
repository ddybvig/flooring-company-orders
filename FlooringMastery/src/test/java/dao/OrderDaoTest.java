/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.InvalidOrderNumberException;

/**
 *
 * @author daler
 */
public class OrderDaoTest {

    OrderDao orderDao = new OrderDaoFileImpl();

    public OrderDaoTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createOrder method, of class OrderDao.
     */
    @Test
    public void testCreateOrderAndGetOrderToEdit() throws Exception {
        String testCustomer = "testCustomer";
        String testState = "OH";
        String testProduct = "carpet";
        BigDecimal testArea = new BigDecimal("100");
        Order testOrder = new Order(testCustomer, testState, testProduct, testArea);
        testOrder.setTaxRate(testArea);
        testOrder.setMaterialCostPerSquareFoot(testArea);
        testOrder.setLaborCostPerSquareFoot(testArea);
        testOrder.setMaterialCost(testArea);
        testOrder.setLaborCost(testArea);
        testOrder.setTax(testArea);
        testOrder.setTotalCost(testArea);
        testOrder = orderDao.createOrder(testOrder);
        Order fromDao = orderDao.getOrderToEdit(LocalDate.now().toString(), testOrder.getOrderNumber());
        assertEquals(testOrder, fromDao);
        orderDao.removeOrder(LocalDate.now().toString(), testOrder.getOrderNumber());
    }

    /**
     * Test of getOrdersByDate method, of class OrderDao.
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        List<Order> ordersByDateTest = orderDao.getOrdersByDate("2000-01-01");
        assertFalse(ordersByDateTest.isEmpty());
    }

    /**
     * Test of editOrder method, of class OrderDao.
     */
    @Test
    public void testEditOrder() throws Exception {
        Order testOrderFromDao = orderDao.getOrderToEdit("2000-01-01", 1);//had this as "2001-01-01 for about 20 debugs til I realized! UGH!!
        String testEditName = "testEdit";
        String testState = "OH";
        String testProduct = "carpet";
        BigDecimal testArea = new BigDecimal("100");
        Order testEdit = new Order(testEditName, testState, testProduct, testArea);
        testEdit.setTaxRate(testArea);
        testEdit.setMaterialCostPerSquareFoot(testArea);
        testEdit.setLaborCostPerSquareFoot(testArea);
        testEdit.setMaterialCost(testArea);
        testEdit.setLaborCost(testArea);
        testEdit.setTax(testArea);
        testEdit.setTotalCost(testArea);
        testEdit.setOrderNumber(1);
        orderDao.editOrder(testEdit, "2000-01-01", 1);
        Order editedOrder = orderDao.getOrderToEdit("2000-01-01", 1);
        assertEquals(editedOrder, testEdit);
    }

    /**
     * Test of removeOrder method, of class OrderDao.
     */
    @Test
    public void testRemoveOrder() throws Exception { //maybe I don't need to even have this since my create/get test uses remove anyway??
        String testCustomer = "testCustomerToRemove";
        String testState = "OH";
        String testProduct = "carpet";
        BigDecimal testArea = new BigDecimal("100");
        Order testOrderToRemove = new Order(testCustomer, testState, testProduct, testArea);
        testOrderToRemove.setTaxRate(testArea);
        testOrderToRemove.setMaterialCostPerSquareFoot(testArea);
        testOrderToRemove.setLaborCostPerSquareFoot(testArea);
        testOrderToRemove.setMaterialCost(testArea);
        testOrderToRemove.setLaborCost(testArea);
        testOrderToRemove.setTax(testArea);
        testOrderToRemove.setTotalCost(testArea);
        testOrderToRemove = orderDao.createOrder(testOrderToRemove);
        Order removedOrder = orderDao.removeOrder(LocalDate.now().toString(), testOrderToRemove.getOrderNumber());
        assertNull(orderDao.getOrderToEdit(LocalDate.now().toString(), removedOrder.getOrderNumber()));
    }

}
