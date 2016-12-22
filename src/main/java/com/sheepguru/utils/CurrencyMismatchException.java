/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

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
