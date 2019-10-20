/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author daler
 */
public class FlooringServiceTest {

    private FlooringService service;

    public FlooringServiceTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", FlooringService.class);
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
     * Test of createOrder method, of class FlooringService.
     */
    @Test
    public void testCreateOrder() throws Exception {
        //good path:
        Order testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("testName");
        testOrder.setState("OH");
        testOrder.setProductType("carpet");
        BigDecimal testArea = new BigDecimal("100");
        testOrder.setArea(testArea);
        testOrder = service.createOrder(testOrder);//I'm pretty sure I need this to return an Order so that I can test the calculations??
        BigDecimal taxRateCheck = new BigDecimal("6.25");
        assertEquals(taxRateCheck, testOrder.getTaxRate());
        BigDecimal materialCostPerSquareFootCheck = new BigDecimal("2.25");
        assertEquals(materialCostPerSquareFootCheck, testOrder.getMaterialCostPerSquareFoot());
        BigDecimal laborCostPerSquareFootCheck = new BigDecimal("2.10");
        assertEquals(laborCostPerSquareFootCheck, testOrder.getLaborCostPerSquareFoot());
        BigDecimal checkMaterialCost = new BigDecimal("225.00");
        BigDecimal checkLaborCost = new BigDecimal("210.00");
        BigDecimal checkTax = new BigDecimal("27.19");
        BigDecimal checkTotalCost = new BigDecimal("462.19");
        assertEquals(checkMaterialCost, testOrder.getMaterialCost());
        assertEquals(checkLaborCost, testOrder.getLaborCost());
        assertEquals(checkTax, testOrder.getTax());
        assertEquals(checkTotalCost, testOrder.getTotalCost());
        //invalid name bad path
        testOrder.setCustomerName("");//testing empty
        try {
            service.createOrder(testOrder);
            fail("expected invalid data exception not thrown");
        } catch (InvalidDataException ex) {
            //expected result
        }
        testOrder.setCustomerName("   "); //testing blank
        try {
            service.createOrder(testOrder);
            fail("expected invalid data exception not thrown");
        } catch (InvalidDataException ex) {
            //expected result
        }
        //invalid state bad path
        testOrder.setCustomerName("testName");
        testOrder.setState("MN");
        try {
            service.createOrder(testOrder);
            fail("expected invalid data exception not thrown");
        } catch (InvalidDataException ex) {
            //expected result
        }
        //invalid product type bad path
        testOrder.setState("OH");
        testOrder.setProductType("potato");
        try {
            service.createOrder(testOrder);
            fail("expected invalid data exception not thrown");
        } catch (InvalidDataException ex) {
            //expected result
        }
    }

    /**
     * Test of getOrdersByDate method, of class FlooringService.
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        //pass through method
    }

    /**
     * Test of getOrderToEdit method, of class FlooringService.
     */
    @Test
    public void testGetOrderToEdit() throws Exception {
        //good path
        Order testOrder = service.getOrderToEdit(LocalDate.EPOCH.toString(), 1);
        assertNotNull(testOrder);
        //invalid order number bad path
        try{
        testOrder = service.getOrderToEdit(LocalDate.EPOCH.toString(), 7);
        fail("expected invalid order number exception not thrown");
        } catch (InvalidOrderNumberException ex) {
            //expected result
        }
    }

    /**
     * Test of editOrder method, of class FlooringService.
     */
    @Test
    public void testEditOrder() throws Exception {
        //good path
        Order unEditedOrder = new Order();
        unEditedOrder.setOrderNumber(1);
        unEditedOrder.setCustomerName("unEdited");
        unEditedOrder.setState("OH");
        unEditedOrder.setProductType("carpet");
        BigDecimal testArea = new BigDecimal("100");
        unEditedOrder.setArea(testArea);
        Order editedOrder = service.editOrder(unEditedOrder, LocalDate.EPOCH.toString(), 1);
        assertEquals(unEditedOrder.getOrderNumber(), editedOrder.getOrderNumber());
        //only bad path would be for an invalid order number, which is covered in the getOrderToEdit method that is within the edit method in the controller
        //basically, it's impossible to get into service.edit with an invalid order number. the getOrderToEdit exception would be thrown first
    }

    /**
     * Test of removeOrder method, of class FlooringService.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        //good path
        Order removedOrder = service.removeOrder(LocalDate.EPOCH.toString(), 1, "y");//testing removing the onlyOrder order in the stub which has an orderNumber of 1
        assertNotNull(removedOrder);
        //bad order number path
        try{
        service.removeOrder(LocalDate.EPOCH.toString(), 22, "y");
        fail("expected invalid ordere number exception not thrown");
        } catch (InvalidOrderNumberException ex) {
            //expected result
        }
        //invalid confirmation input bad path
        try {
            service.removeOrder(LocalDate.EPOCH.toString(), 1, "potato");
            fail("expected invalid data exception not thrown");
        } catch (InvalidDataException ex) {
            //expected result
        }
    }

}
