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

package com.buffalokiwi.aerodrome.jet.products;


/**
 * Product Code Type used with the ProductCodes
 *
 * @author john Quinn
 */
public enum ProductCodeType
{
  GTIN14( "GTIN-14", 0 ),
  EAN( "EAN", 1 ),
  ISBN( "ISBN-10", 4 ),
  ISBN13( "ISBN-13", 3 ),
  UPC( "UPC", 2 ),
  
  /**
   * This is not a valid product code.  Do not send this value under
   * standard_product_codes.
   */
  ASIN( "ASIN", 5 );

  /**
   * Type
   */
  private final String type;

  /**
   * Sort order
   */
  private final int sort;

  private final static ProductCodeType[] values = values();
  
  /**
   * Attempt to retrieve a ProductCodeType by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ProductCodeType fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ProductCodeType c : values )
    {
      if ( c.getType().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
  
  /**
   * Create a new ProductCodeType instance
   * @param type Jet constant value
   * @param sort Sort order
   */
  ProductCodeType( final String type, final int sort )
  {
    this.type = type;
    this.sort = sort;
  }


  /**
   * Retrieve the sort index
   * @return index
   */
  public int getSort()
  {
    return sort;
  }


  /**
   * Retrieve the product type string
   * @return type
   */
  public String getType()
  {
    return type;
  }
  
  
  @Override
  public String toString()
  {
    return type;
  }
}

