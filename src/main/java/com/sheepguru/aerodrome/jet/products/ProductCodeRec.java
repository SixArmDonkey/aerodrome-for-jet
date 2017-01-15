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

import com.sheepguru.aerodrome.jet.Jsonable;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Product Codes attached to this product
 * @author John Quinn
 */
public class ProductCodeRec implements Comparable, Jsonable
{
  /**
   * A standard, unique identifier for a product. ISBN-10, ISBN-13, UPC,
   * EAN and GTIN-14 are accepted.
   * <br />
   * Valid Values:
   * <ul>
   * <li>If standard_product_code_type is 'GTIN-14' - must be 14 digits</li>
   * <li>If standard_product_code_type is 'EAN' - must be 13 digits</li>
   * <li>If standard_product_code_type is 'ISBN-10' - must be 10 digits</li>
   * <li>If standard_product_code_type is 'ISBN-13' - must be 13 digits</li>
   * <li>If standard_product_code_type is'UPC' - must be 12 digits</li>
   * </ul>
   */
  private final String standardProductCode;

  /**
   * Indicate the type of standard_product_code that was used.
   * Valid Values
   * GTIN-14, EAN, ISBN-10, ISBN-13, UPC
   */
  private final ProductCodeType standardProductCodeType;

  
  /**
   * Creates a deep copy of this object 
   * @return 
   */
  public ProductCodeRec createCopy()
  {
    return new ProductCodeRec( standardProductCode, standardProductCodeType );
  }
  
  
  /**
   * Create a new ProductCode instance
   * @param code The code
   * @param type The code type
   * @throws IllegalArgumentException
   */
  public ProductCodeRec( final String code, final ProductCodeType type )
  {
    if ( code == null )
      throw new IllegalArgumentException( "code cannot be null" );
    else if ( type == null )
      throw new IllegalArgumentException( "type cannot be null" );

    if ( !code.isEmpty())
    {
      switch( type )
      {
        case GTIN14:
          if ( code.length() != 14 )
            throw new IllegalArgumentException( "code must be 14 digits" );
        break;

        case EAN: //..fall through
        case ISBN13:
          if ( code.length() != 13 )
            throw new IllegalArgumentException( "code must be 13 digits" );
        break;

        case ISBN:
          if ( code.length() != 10 )
            throw new IllegalArgumentException( "code must be 10 digits" );
        break;

        case UPC:
          if ( code.length() != 12 )
            throw new IllegalArgumentException( "code must be 12 digits" );
        break;

        default:
          throw new IllegalArgumentException( "Unsupported product code type" );
      }
    }

    standardProductCode = code;
    standardProductCodeType = type;
  }


  /**
   * Sort this
   * @param code Code to compare to
   * @return order
   */
  @Override
  public int compareTo( Object code )
  {
    ProductCodeRec c1 = (ProductCodeRec)code;

    if ( standardProductCodeType.getSort() > c1.getProductCodeType().getSort())
      return 1;
    else if ( standardProductCodeType.getSort() < c1.getProductCodeType().getSort())
      return -1;
    return 0;
  }


  /**
   * Retrieve the product code
   * @return Product Code
   */
  public String getProductCode()
  {
    return standardProductCode;
  }


  /**
   * Retrieve the product code type
   * @return product code type
   */
  public ProductCodeType getProductCodeType()
  {
    return standardProductCodeType;
  }


  /**
   * Turn this into a json object
   * @return JSON
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "standard_product_code", standardProductCode )
      .add( "standard_product_code_type", standardProductCodeType.getType())
      .build();
  }
}