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
public class BulkVariationFileGenerator extends BulkUploadFileGenerator
{
  /**
   * Create a new BulkVariationFileGenerator
   * @param outputFile Filename to write to
   */
  public BulkVariationFileGenerator( final File outputFile ) throws IOException  
  {
    super( outputFile );
  }
  
  
  /**
   * Write a product variation to the file
   * @param rec variation data
   */
  public void writeLine( final ProductVariationGroupRec rec )
  {
    if ( rec == null )
      throw new IllegalArgumentException( "rec can't be null" );
        
    super.writeLine( rec.getParentSku(), rec.toJSON());
  }  
}
