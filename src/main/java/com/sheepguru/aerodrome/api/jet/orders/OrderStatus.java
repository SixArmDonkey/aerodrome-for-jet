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

package com.sheepguru.aerodrome.api.jet.orders;

/**
 *
 * @author john
 */
public enum OrderStatus 
{
  /**
   * None specified
   */
  NONE( "" ),
  
  /**
   * order was created by Jet.com but not released for fulfillment
   */
  CREATED( "created" ),
  
  /**
   * order ready to be acknowledged by merchant
   */
  READY( "ready" ),
  
  /**
   * order was acknowledged by merchant
   */
  ACK( "acknowledged" ),
  
  /**
   * one part of the order has been shipped or cancelled
   */
  IN_PROGRESS( "inprogress" ),
  
  /**
   * all parts of the order have shipped or cancelled
   */
  COMPLETE( "complete" );
  
  /**
   * The jet text
   */
  private final String text;
  
  private static final OrderStatus[] values = values();
  
  /**
   * Attempt to retrieve a OrderStatus by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static OrderStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final OrderStatus s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }
  
  
  /**
   * Create a new OrderStatus enum instance
   * @param text The text for jet api 
   */
  OrderStatus( final String text )
  {
    this.text = text;
  }


  /**
   * Retrieve the jet api text
   * @return value for jet 
   */
  public String getText()
  {
    return text;
  }
}
