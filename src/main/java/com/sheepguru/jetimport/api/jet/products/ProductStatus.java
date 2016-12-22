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

/**
 * The product status retrieved from Jet
 *
 * @author John Quinn
 */
public enum ProductStatus
{
  /**
   * Status unknown
   */
  NONE( "" ),

  /**
   * he product is listed on Jet.com and can be purchased by a customer
   */
  AVAILABLE( "Available For Purchase" ),

  /**
   * The product can be listed on Jet.com and requires inventory
   */
  READY( "Ready To List" ),

  /**
   * More data is needed to list the product on Jet.com
   */
  DATA_NEEDED( "Listing Data Needed" ),

  /**
   * The SKU is current being processed in Jet's product catalog
   */
  PROCESSING( "Processing" );

  private static ProductStatus[] values = values();

  /**
   * Retrieve a ProductStatus by Jet API value
   * @param value value
   * @return status
   */
  public static ProductStatus fromValue( final String value )
  {
    for ( ProductStatus s : values )
    {
      if ( value.equals( s.getValue()))
        return s;
    }

    throw new IllegalArgumentException( value + " is not a valid value for ProductStatus" );
  }

  /**
   * The jet api value
   */
  private final String val;

  /**
   * Create a new ProductStatus instance
   * @param val jet api value
   */
  ProductStatus( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return value
   */
  public String getValue()
  {
    return val;
  }
}
