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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

/**
 * Used for generating files used for bulk uploads.
 * 
 * @author John Quinn
 */
public class BulkUploadFileGenerator implements IBulkUploadFileGenerator
{
  /**
   * output file 
   */
  private final File outputFile;
  private final JsonGenerator output;
  
  
  /**
   * This will simply gzip some inputstream and write it to outputFile.
   * The input stream is NOT opened or closed.
   * @param in Input 
   * @throws IOException
   */
  public static void gzipStream( final InputStream in, final File outFile ) throws IOException
  {
    checkOutputFile( outFile );
    byte[] buff = new byte[8192];
     
    int len;
    
    try ( final GZIPOutputStream out = new GZIPOutputStream( new FileOutputStream( outFile ))) 
    {      
      while (( len = in.read( buff )) > 0 ) 
      {
        out.write( buff, 0, len );
      }
    }
  }  
  
  
  /**
   * Create a new BulkUploadFileGenerator
   * @param outputFile Filename to write to
   */
  public BulkUploadFileGenerator( final File outputFile ) throws IOException
  {
    checkOutputFile( outputFile );
    this.outputFile = outputFile;    
    this.output = getGzipJsonOutputStream( outputFile );
    this.output.writeStartObject();
  }
  
  private static void checkOutputFile( final File f )
  {
    if ( f == null )
      throw new IllegalArgumentException( "outputFile can't be null" );
    else if ( !new File( f.getParent()).canWrite())
      throw new IllegalArgumentException( "Can't write to " + f.getPath());
  }
          
  
  @Override
  public void close()
  {
    output.writeEnd();
    output.close();
  }
  
  
  /**
   * Write some line to json
   * @param merchantSku
   * @param line 
   */
  @Override
  public void writeLine( final String merchantSku, final JsonValue line )
  {
    if ( merchantSku == null )
      throw new IllegalArgumentException( "merchantSku can't be null" );
    else if ( line == null )
      throw new IllegalArgumentException( "line can't be null" );
    
    output.write( merchantSku, line );
  }
  
  
  /**
   * Creates a gzipped output stream to some file on disk and provides a 
   * JsonGenerator to write some json to that file.   
   * @param outputFile Where to write 
   * @return Resource 
   * @throws IOException if it cant open the file 
   */
  private JsonGenerator getGzipJsonOutputStream( final File outputFile ) 
    throws IOException
  {
    return Json.createGenerator( 
      new GZIPOutputStream( new FileOutputStream( outputFile ))
    );
  }   
}
