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

import static com.sheepguru.jetimport.api.jet.ProductTaxCode.values;

/**
 * Represents the type of file being uploaded.
 * 
 * @author John Quinn
 */
public enum BulkUploadFileType 
{
  /**
   * Send a batch of merchant sku's 
   */
  MERCHANT_SKUS( "MerchantSKUs" ),
  
  /**
   * Send a batch of inventory updates 
   */
  INVENTORY( "Inventory" ),
  
  /**
   * Send a batch of product price updates
   */
  PRICE( "price" ),
  
  /**
   * Send a batch of product variations 
   */
  VARIATION( "Variation" ),
  
  /**
   * Send a batch of product returns exceptions 
   */
  RETURNS_EXCEPTION( "ReturnsException" ),
  
  /**
   * Send a batch of product shipping exceptions
   */
  SHIPPING_EXCEPTION( "ShippingException" ),
  
  /**
   * Send a batch of sku's to archive 
   */
  ARCHIVE_SKU( "Archive" );
  
  
  /**
   * The Jet API Text to send 
   */
  private final String text;
  
  private static final BulkUploadFileType[] values = values();
  
  /**
   * Create an instance from Jet text
   * @param text text
   * @return instance 
   * @throws IllegalArgumentException if text isn't found 
   */
  public static BulkUploadFileType fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final BulkUploadFileType c : values )
    {
      if ( c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );    
  }
  
  
  /**
   * Create a new BulkUploadFileType instance 
   * @param text The Jet API Text 
   */
  BulkUploadFileType( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the text to send to Jet 
   * @return text 
   */
  public String getText()
  {
    return text;
  }
}