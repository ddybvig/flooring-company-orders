/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.TaxRate;
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
public class TaxDaoFileImpl implements TaxDao {

    public static final String TAX_FILE = "taxes.txt";
    public static final String DELIMITER = ",";

    List<TaxRate> taxes = new ArrayList<>();

    @Override
    public List<TaxRate> getAllTaxRates() throws FlooringDaoException {
        loadTaxRates();
        return taxes;
    }

    @Override
    public BigDecimal getTaxRate(String state) throws FlooringDaoException {
        getAllTaxRates();
        for (TaxRate t : taxes) {
            if (t.getState().equalsIgnoreCase(state)) {
                return t.getTaxRate();
            }
        }
        return null;
    }

    public TaxRate unmarshallTaxRate(String taxRateAsText) {
        String[] taxRateTokens = taxRateAsText.split(DELIMITER);
        TaxRate taxRateFromFile = new TaxRate();
        taxRateFromFile.setState(taxRateTokens[0]);
        BigDecimal taxRateBD = new BigDecimal(taxRateTokens[1]);
        taxRateFromFile.setTaxRate(taxRateBD);
        return taxRateFromFile;
    }

    public void loadTaxRates() throws FlooringDaoException {
        Scanner sc;

        try {
            // Create Scanner for reading the file
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringDaoException("Could not load tax data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentTaxRate holds the most recent taxRate unmarshalled
        TaxRate currentTaxRate;
        // Go through TAX_FILE line by line, decoding each line into a 
        // TaxRate object by calling the unmarshallTaxRate method.
        // Process while we have more lines in the file
        while (sc.hasNextLine()) {
            // get the next line in the file
            currentLine = sc.nextLine();
            // unmarshall the line into a TaxRate
            currentTaxRate = unmarshallTaxRate(currentLine);

            taxes.add(currentTaxRate);
        }
        // close scanner
        sc.close();
    }

}
