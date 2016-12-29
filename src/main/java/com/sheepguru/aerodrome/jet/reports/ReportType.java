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
package com.sheepguru.aerodrome.jet.reports;

/**
 *
 * @author john
 */
public enum ReportType
{
  /**
   * Will return the status and substatuses of all SKUs in merchant catalog
   */
  PRODUCT_STATUS( "ProductStatus" ),
  
  /**
   * Assuming this is sales data, it is undocumented on jet.
   */
  SALES_DATA( "SalesData" );
  

  /**
   * Enum values 
   */
  private static final ReportType[] values = values();
  
  
  /**
   * Attempt to create a ReportType by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ReportType fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ReportType c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + String.valueOf( text ));
  }    
  
  /**
   * Jet text
   */
  private final String text;
  
  
  /**
   * Constructor
   * @param text jet text
   */
  ReportType( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text
   * @return jet text
   */
  public String getText()
  {
    return text;
  }      
}
