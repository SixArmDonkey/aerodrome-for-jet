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

package com.buffalokiwi.aerodrome.jet.orders;

/**
 *
 * @author john
 */
public enum OrderExceptionState 
{
  /**
   * None specified 
   */
  NONE( "" ),
  
  /**
   * 
   */
  TOO_MANY_CANCELED( "exception - too many units cancelled" ),
  
  /**
   * 
   */
  MANUAL_CANCEL_TO_COMPLETE( "exception - jet manual canceled to complete state" ),
  
  /**
   * 
   */
  TOO_MANY_SHIPPED( "exception - too many units shipped" ),
  
  /**
   * 
   */
  REJECTED( "exception - order rejected" ),
  
  /**
   * 
   */
  RESOLVED( "resolved" );
  
  
  /**
   * Jet text
   */
  private final String text;
  
  
  private static final OrderExceptionState[] values = values();
  
  /**
   * Attempt to retrieve a OrderExceptionState by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static OrderExceptionState fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final OrderExceptionState s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }  
  
  
  /**
   * Create a new OrderExceptionState enum instance
   * @param text Jet text
   */
  OrderExceptionState( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text
   * @return text
   */
  public String getText()
  {
    return text;
  }
}
