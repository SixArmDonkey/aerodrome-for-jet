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
 * Order item acknowledgement status 
 * @author John Quinn
 */
public enum ItemAckStatus 
{
  /**
   * Not specified 
   */
  NONE( "" ),
  
  /**
   * Can be fulfilled
   */
  FULFILLABLE( "fulfillable" ),
  
  /**
   * Cannot be fulfilled 
   * The documentation clearly states "nonfulfillable", but 
   * the API is returning "nonfulfillable - no inventory".
   */
  NOT_FULFILLABLE( "nonfulfillable - no inventory" ),
 
  /**
   * An invalid sku was in the order - cannot be fulfilled.
   */
  INVALID_SKU( "nonfulfillable - invalid merchant SKU" );
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  /**
   * Values cache
   */
  private static final ItemAckStatus[] values = values();

  
  /**
   * Attempt to retrieve a ItemAckStatus by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static ItemAckStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ItemAckStatus s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }
    
  
  /**
   * Create a new AckStatus instance 
   * @param text jet api text
   */
  ItemAckStatus( final String text )
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
