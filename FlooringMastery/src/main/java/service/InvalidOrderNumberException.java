/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author daler
 */
public class InvalidOrderNumberException extends Exception {
    
    public InvalidOrderNumberException(String message) {
        super(message);
    }
    
    public InvalidOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }
    
}