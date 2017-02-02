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
package com.sheepguru.aerodrome.jet.products;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author john
 */
public class BulkInventoryFileGenerator extends BulkUploadFileGenerator
{
  /**
   * Create a new BulkInventoryFileGenerator
   * @param outputFile Filename to write to
   */
  public BulkInventoryFileGenerator( final File outputFile ) throws IOException  
  {
    super( outputFile );
  }
  
  
  /**
   * Write some inventory 
   * @param sku The merchant sku 
   * @param rec inventory data to write 
   */
  public void writeLine( final String sku, final ProductInventoryRec rec )
  {
    if ( rec == null )
      throw new IllegalArgumentException( "rec can't be null" );
    else if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "Merchant Sku can't be null or empty" );
        
    super.writeLine( sku, rec.toJSON());
  }  
}
