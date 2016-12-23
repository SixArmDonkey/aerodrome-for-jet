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

package com.sheepguru.aerodrome.jet.products;

/**
 *
 * @author john
 */
public class ValidateException extends Exception {

  /**
   * Creates a new instance of <code>ValidateException</code> without detail
   * message.
   */
  public ValidateException() {
  }

  /**
   * Constructs an instance of <code>ValidateException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public ValidateException(String msg) {
    super(msg);
  }
}
