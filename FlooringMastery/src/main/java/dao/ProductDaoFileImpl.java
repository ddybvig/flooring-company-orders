/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author daler
 */
public class ProductDaoFileImpl implements ProductDao {

    public static final String PRODUCTS_FILE = "products.txt";
    public static final String DELIMITER = ",";

    List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getAllProducts() throws FlooringDaoException {
        loadProducts();
        return products;
    }

    @Override
    public Product getProduct(String name) throws FlooringDaoException {
        getAllProducts();
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        Product productFromFile = new Product();
        productFromFile.setName(productTokens[0]);
        BigDecimal materialCostBD = new BigDecimal(productTokens[1]);
        productFromFile.setCostPerSquareFoot(materialCostBD);
        BigDecimal laborCostBD = new BigDecimal(productTokens[2]);
        productFromFile.setLaborCostPerSquareFoot(laborCostBD);
        return productFromFile;
    }

    public void loadProducts() throws FlooringDaoException {
        Scanner sc;

        try {
            // Create Scanner for reading the file
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringDaoException("Could not load product data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentProduct holds the most recent taxRate unmarshalled
        Product currentProduct;
        // Go through PRODUCTS_FILE line by line, decoding each line into a 
        // Product object by calling the unmarshallProduct method.
        // Process while we have more lines in the file
        while (sc.hasNextLine()) {
            // get the next line in the file
            currentLine = sc.nextLine();
            // unmarshall the line into a TaxRate
            currentProduct = unmarshallProduct(currentLine);

            products.add(currentProduct);
        }
        // close scanner
        sc.close();
    }
}
