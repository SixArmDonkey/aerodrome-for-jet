/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet.products;

import static com.sheepguru.jetimport.api.jet.products.ShipExceptionType.values;

/**
 * Shipping exception type for shipping exception item.
 * Indicates if you want the product to be shipped exclusively (only with) or
 * restrictively (not including) via the shipping level of shipping method
 * provided
 *
 * @author John Quinn
 */
public enum ShipExceptionType
{
  /**
   * No exceptions
   */
  NONE( "" ),

  /**
   * The product should only be shipped using the shipping method or
   * shipping level provided
   */
  EXCLUSIVE( "exclusive" ),

  /**
   * The product cannot be shipped using the shipping method or shipping
   * level provided
   */
  RESTRICTED( "restricted" ),
  
  /**
   * The product will be priced normally under the Jet.com pricing algorithm. 
   */
  INCLUDE( "include" );

  /**
   * Jet value
   */
  private final String val;

  private static ShipExceptionType[] values = values();
  
  /**
   * Attempt to create a ShipExceptionType by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShipExceptionType fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShipExceptionType c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }      

  
  /**
   * Create a new ShipExceptionType
   * @param val jet value
   */
  ShipExceptionType( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return value
   */
  public String getText()
  {
    return val;
  }
}