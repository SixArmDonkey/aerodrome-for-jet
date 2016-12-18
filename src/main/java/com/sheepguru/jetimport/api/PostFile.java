
package com.sheepguru.jetimport.api;

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