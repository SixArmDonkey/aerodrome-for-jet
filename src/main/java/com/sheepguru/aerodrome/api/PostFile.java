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

package com.sheepguru.aerodrome.api;

import java.io.File;
import org.apache.http.entity.ContentType;

/**
 * A stupid class for posting a file via HttpClient 
 * @author John Quinn
 */
public class PostFile 
{
  /**
   * File to upload
   */
  private final File file;
  
  /**
   * Content type for the file 
   */
  private final ContentType contentType;
  
  /**
   * Filename for the file 
   */
  private final String filename;
  
  private final String contentEncoding;
  
  
  public PostFile( final File file, final ContentType contentType, 
    final String contentEncoding, final String filename )
  {
    if ( file == null )
      throw new IllegalArgumentException( "file cannot be null" );
    else if ( contentType == null )
      throw new IllegalArgumentException( "contentType cannot be null" );
    else if ( contentEncoding == null )
      throw new IllegalArgumentException( "contentEncoding cannot be null" );
    else if ( filename == null )
      throw new IllegalArgumentException( "filename cannot be null" );
    
    this.file = file;
    this.contentType = contentType;
    this.contentEncoding = contentEncoding;
    this.filename = filename;
  }
  
  
  public File getFile()
  {
    return file;
  }
  
  public ContentType getContentType()
  {
    return contentType;
  }
  
  
  public String getFilename()
  {
    return filename;
  }
  
  
  public boolean hasFilename()
  {
    return !filename.isEmpty();
  }
  
  
  public boolean hasContentEncoding()
  {
    return !contentEncoding.isEmpty();
  }
  
  public String getContentEncoding()
  {
    return contentEncoding;
  }
}
