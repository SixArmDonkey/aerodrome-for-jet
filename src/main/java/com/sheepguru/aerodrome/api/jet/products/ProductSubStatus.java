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

package com.sheepguru.aerodrome.api.jet.products;

/**
 * The Jet API Product sub status
 *
 * @author John Quinn
 */
public enum ProductSubStatus
{
  /**
   * Unknown status
   */
  NONE( "" ),

  /**
   *  Additional preferred attributes are requested but not required
   * (the product can be listed without clearing this sub status)
   */
  DATA_NEEDED( "Attribute Data Needed" ),

  /**
   * A price is needed to list the product on Jet.com
   */
  PRICE_NEEDED( "Price Needed" ),

  /**
   * A main image is needed to list the product on Jet.com
   */
  IMAGE_NEEDED( "Image Needed" );


  private static ProductSubStatus[] values = values();
  
  /**
   * Retrieve a Product sub status from the jet api value
   * @param val value
   * @return sub status
   */
  public static ProductSubStatus fromValue( final String val )
  {
    for ( ProductSubStatus s : values )
    {
      if ( val.equals( s.getValue()))
        return s;
    }

    throw new IllegalArgumentException( val + " is not a valid value for ProductSubStatus" );
  }


  /**
   * Jet API Value
   */
  private final String val;

  /**
   * Create a new ProductSubStatus instance
   * @param val Jet api value
   */
  ProductSubStatus( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the jet api value
   * @return value
   */
  public String getValue()
  {
    return val;
  }
}
