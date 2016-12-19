
package com.sheepguru.jetimport.api.jet.products;

import com.sheepguru.jetimport.api.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a response from the JetFileId command.
 * @author John Quinn
 */
public class FileIdRec 
{
  /**
   * A processing status from jet api about a bulk uploaded file.
   */
  public static enum UploadStatus 
  {
    /**
     * Unknown status.
     */
    UNKNOWN( "" ),
    
    /**
     * Jet has the file but hasn't started processing yet.
     */
    ACKNOWLEDGED( "Acknowledged" ),
    
    /**
     * Jet is currently processing whatever file was uploaded.
     */
    PROCESSING( "Processing" ),
    
    /**
     * The file is done, but there were errors.
     */
    PROCESSED_WITH_ERRORS( "Processed with errors" ),
    
    /**
     * File processed successfully, no action needed.
     */
    PROCESS_SUCCESS( "Processed successfully" );
    
    /**
     * The jet text
     */
    private final String text;
    
    private static final UploadStatus[] values = values();

    /**
     * Create an instance from Jet text
     * @param text text
     * @return instance 
     * @throws IllegalArgumentException if text isn't found 
     */
    public static UploadStatus fromText( final String text )
      throws IllegalArgumentException
    {
      for ( final UploadStatus c : values )
      {
        if ( c.getText().equalsIgnoreCase( text ))
          return c;
      }

      throw new IllegalArgumentException( "Invalid value " + text );    
    }

    
    /**
     * Create a new UploadStatus enum instance 
     * @param text Jet api text
     */
    UploadStatus( final String text )
    {
      this.text = text;
    }
    
    
    /**
     * Retrieve the jet text
     * @return  jet Text
     */
    public String getText()
    {
      return text;
    }
  }
  
  /**
   * The URL where the file was placed
   */
  private final String url;
  
  /**
   * Type of file that was uploaded
   */
  private final BulkUploadFileType fileType;
  
  /**
   * Merchant's name for the file
   */
  private final String fileName;
  
  /**
   * The date-time the file was received
   */
  private final String received;
  
  /**
   * The date-time the file started processing
   */
  private final String processingStart;
  
  /**
   * The date-time the file finished processing
   */
  private final String processingEnd;
  
  /**
   * The status of the file
   */
  private final UploadStatus status;
  
  /**
   * Number of errors encountered when processing the file
   */
  private final int errorCount;
  
  /**
   * URL to the file with errors
   */
  private final String errorUrl;
  
  /**
   * First 20 errors with description of location in the file
   */
  private final List<String> errorExcerpt;
  
  /**
   * An identification assigned by Jet to track the file. 
   * This identification retrieves the file upload details 
   * including processing errors
   */
  private final String jetFileId;
  
  
  /**
   * Convert jet json into an instance of this.
   * @param json Jet json 
   * @return instance 
   */
  public static FileIdRec fromJSON( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final JsonArray errors = json.getJsonArray( "error_excerpt" );
    final List<String> errorList = new ArrayList<>();
    
    if ( errors != null )
    {
      for ( int i = 0; i < errors.size(); i++ )
      {
        errorList.add( errors.getString( i ));
      }
    }
    
    return new FileIdRec(
      json.getString( "url", "" ),
      BulkUploadFileType.fromText( json.getString( "file_type", "" )),
      json.getString( "file_name", "" ),
      json.getString( "received", "" ),
      json.getString( "processing_start", "" ),
      json.getString( "processing_end", "" ),
      UploadStatus.fromText( json.getString( "status", "" )),
      json.getInt( "error_count", 0 ),
      json.getString( "error_url", "" ),
      errorList,
      json.getString( "jet_file_id", "" )
    );
  }
  
  
  /**
   * Create a new JetFileId instance representing a response from the JetFileId
   * request.
   * @param url The URL where the file was placed
   * @param fileType Type of file that was uploaded
   * @param fileName Merchant's name for the file
   * @param received The date-time the file was received
   * @param processingStart The date-time the file started processing
   * @param processingEnd The date-time the file finished processing
   * @param status The status of the file
   * @param errorCount Number of errors encountered when processing the file
   * @param errorUrl URL to the file with errors
   * @param errorExcerpt First 20 errors with description of location in the 
   * file
   * @param jetFileId An identification assigned by Jet to track the file. This 
   * identification retrieves the file upload details including processing 
   * errors
   */
  public FileIdRec( 
    final String url, 
    final BulkUploadFileType fileType, 
    final String fileName, 
    final String received, 
    final String processingStart,
    final String processingEnd, 
    final UploadStatus status, 
    final int errorCount,
    final String errorUrl, 
    final List<String> errorExcerpt, 
    final String jetFileId )
  {
    Utils.checkNullEmpty( url, "url" );
    Utils.checkNull( fileType, "fileType" );
    Utils.checkNullEmpty( fileName, "fileName" );
    Utils.checkNull( received, "received" );
    Utils.checkNull( processingStart, "processingStart" );
    Utils.checkNull( processingEnd, "processingEnd" );
    Utils.checkNull( status, "status" );
    Utils.checkNull( errorUrl, "errorUrl" );
    Utils.checkNull( errorExcerpt, "errorExcerpt" );
    
    this.url = url;
    this.fileType = fileType;
    this.fileName = fileName;
    this.received = received;
    this.processingStart = processingStart;
    this.processingEnd = processingEnd;
    this.status = status;
    this.errorCount = errorCount;
    this.errorUrl = errorUrl;
    this.errorExcerpt = Collections.unmodifiableList( errorExcerpt );
    this.jetFileId = jetFileId;
  }
  
  
  /**
   * The URL where the file was placed
   * @return the url
   */
  public String getUrl() 
  {
    return url;
  }

  
  /**
   * Type of file that was uploaded
   * @return the fileType
   */
  public BulkUploadFileType getFileType() 
  {
    return fileType;
  }
  

  /**
   * Merchant's name for the file
   * @return the fileName
   */
  public String getFileName() 
  {
    return fileName;
  }

  
  /**
   * The date-time the file was received
   * @return the received
   */
  public String getReceived() 
  {
    return received;
  }

  
  /**
   * The date-time the file started processing
   * @return the processingStart
   */
  public String getProcessingStart() 
  {
    return processingStart;
  }

  
  /**
   * The date-time the file finished processing
   * @return the processingEnd
   */
  public String getProcessingEnd() 
  {
    return processingEnd;
  }

  
  /**
   * The status of the file
   * @return the status
   */
  public UploadStatus getStatus() 
  {
    return status;
  }
  

  /**
   * Number of errors encountered when processing the file
   * @return the errorCount
   */
  public int getErrorCount() 
  {
    return errorCount;
  }
  

  /**
   * URL to the file with errors
   * @return the errorUrl
   */
  public String getErrorUrl() 
  {
    return errorUrl;
  }
  

  /**
   * First 20 errors with description of location in the file
   * @return the errorExcerpt
   */
  public List<String> getErrorExcerpt() 
  {
    return errorExcerpt;
  }

  
  /**
   * An identification assigned by Jet to track the file. This identification 
   * retrieves the file upload details including processing errors
   * @return the jetFileId
   */
  public String getJetFileId() 
  {
    return jetFileId;
  }  
  
  
  /**
   * Turn this into jet json 
   * @return json 
   */
  public JsonObject toJson()
  {
    //..Add the errors to an array 
    final JsonArrayBuilder errors = Json.createArrayBuilder();
    
    for ( final String error : errorExcerpt )
    {
      errors.add( error );
    }
    
    //..Output object builder 
    final JsonObjectBuilder b = Json.createObjectBuilder();
        
    //..Add it all to the json object and return 
    return b.add( "url", url )
     .add( "file_type", fileType.getText())
     .add( "file_name", fileName )
     .add( "received", received )
     .add( "processing_start", processingStart )
     .add( "processing_end", processingEnd )
     .add( "status", status.getText())
     .add( "error_count", errorCount )
     .add( "error_url", errorUrl )
     .add( "error_excerpt", errors.build())
     .add( "jet_file_id", jetFileId )
     .build();
  }    
}