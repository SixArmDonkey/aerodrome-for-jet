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

package com.sheepguru.aerodrome.jet;

/**
 * Service level for the shipping exception item
 * @author John Quinn
 */
public enum ShippingServiceLevel
{
  /**
   * Not Specified
   */
  NONE( "" ),

  /**
   * Ships 2 day
   */
  SECOND_DAY( "Second Day" ),

  /**
   * Ships overnight
   */
  NEXT_DAY( "Next Day" ),

  /**
   * Scheduled?
   */
  SCHEDULED( "Scheduled (freight)" ),

  /**
   * Expedited shipping
   */
  EXPEDITED( "Expedited" ),

  /**
   * Standard shipping
   */
  STANDARD( "Standard" ),
  
  PRIORITY( "Priority" ),
  
  FIVE_TO_10_DAY( "5 to 10 Day" ),

  ELEVEN_TO_20_DAY( "11 to 20 Day" ),

  SCHEDULED_11_TO_20_DAY( "Scheduled (freight 11 to 20 day)" );

  /**
   * Jet API constant value
   */
  private final String val;
  

  private static final ShippingServiceLevel[] values = values();
  
  
  /**
   * Attempt to create a ServiceLevel by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShippingServiceLevel fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShippingServiceLevel c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }      
  
  /**
   * Create a new ServiceLevel instance
   * @param val
   */
  ShippingServiceLevel( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return jet api value
   */
  public String getText()
  {
    return val;
  }
  
  
  @Override
  public String toString()
  {
    return val;
  }
}
