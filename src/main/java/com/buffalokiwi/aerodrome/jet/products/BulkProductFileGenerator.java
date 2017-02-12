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
package com.buffalokiwi.aerodrome.jet.products;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author john
 */
public class BulkProductFileGenerator extends BulkUploadFileGenerator
{
  /**
   * Create a new BulkProductFileGenerator
   * @param outputFile Filename to write to
   */
  public BulkProductFileGenerator( final File outputFile ) throws IOException  
  {
    super( outputFile );
  }
  
  
  /**
   * Write a product to the bulk upload file
   * @param pRec Product to write 
   */
  public void writeLine( final ProductRec pRec )
  {
    if ( pRec == null )
      throw new IllegalArgumentException( "pRec can't be null" );
    else if ( pRec.getMerchantSku().isEmpty())
      throw new IllegalArgumentException( "Merchant Sku can't be empty" );
    
    
    super.writeLine( pRec.getMerchantSku(), pRec.toJSON());
  }
}
