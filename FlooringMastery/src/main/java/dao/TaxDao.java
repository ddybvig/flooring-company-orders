/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TaxRate;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author daler
 */
public interface TaxDao {
   
    public BigDecimal getTaxRate(String state) throws FlooringDaoException;
    
    public List<TaxRate> getAllTaxRates() throws FlooringDaoException;
    
}
