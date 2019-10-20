/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author daler
 */
public interface ProductDao {
    
    public List<Product> getAllProducts() throws FlooringDaoException;
    
    public Product getProduct(String name) throws FlooringDaoException;
    
}
