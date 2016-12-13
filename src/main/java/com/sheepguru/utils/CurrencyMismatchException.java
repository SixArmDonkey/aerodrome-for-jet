
package com.sheepguru.utils;

/**
 * An exception that is thrown when currencies do not match 
 * @author John Quinn
 */
public class CurrencyMismatchException extends RuntimeException
{

    /**
     * Creates a new instance of <code>CurrencyMismatchException</code> without detail message.
     */
    public CurrencyMismatchException() {
    }


    /**
     * Constructs an instance of <code>CurrencyMismatchException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CurrencyMismatchException(String msg) {
        super(msg);
    }
}
