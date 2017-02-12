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

import com.buffalokiwi.aerodrome.jet.JsonableBoolean;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author john
 */
public class BulkArchiveFileGenerator extends BulkUploadFileGenerator
{
  /**
   * Create a new BulkArchiveFileGenerator
   * @param outputFile Filename to write to
   */
  public BulkArchiveFileGenerator( final File outputFile ) throws IOException  
  {
    super( outputFile );
  }
  
  
  /**
   * Write shipping exceptions
   * @param sku sku 
   * @param rec data
   */
  public void writeLine( final String sku, final JsonableBoolean rec )
  {
    if ( sku == null || sku.isEmpty())
      throw new IllegalArgumentException( "sku can't be null or empty" );
    else if ( rec == null )
      throw new IllegalArgumentException( "rec can't be null" );
        
    super.writeLine( sku, rec.toJSON());
  }  
}