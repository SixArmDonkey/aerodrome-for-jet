/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.sheepguru.aerodrome.jet.orders;


/**
 * A refund status 
 * @author John Quinn
 */
public enum RefundStatus
{
  /**
   * Not specified - do not send this value to jet.
   */
  NONE( "" ),
          
  /**
   * Refund was created by merchant 
   */
  CREATED( "Created" ),
          
  /**
   * Jet is processing 
   */
  PROCESSING( "Processing" ),
          
  /**
   * Refund accepted
   */
  ACCEPTED( "Accepted" ),
          
  /**
   * Refund rejected
   */
  REJECTED( "Rejected" );
  
  
  /**
   * Enum values 
   */
  private static final RefundStatus[] values = values();
  
  
  /**
   * Attempt to create a RefundStatus by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static RefundStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final RefundStatus c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
    
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  
  /**
   * Create dat ish
   * @param text jet text
   */
  RefundStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text
   * @return jet text
   */
  public String getText()
  {
    return text;
  }  
}
