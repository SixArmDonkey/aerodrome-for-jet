/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APIResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * Represents a response from the Jet API.  
 * @author John Quinn
 */
public class JetAPIResponse extends APIResponse
{

  /**
   * HTTP Response codes for the Jet API 
   */
  public static enum ResponseCode
  {
    UNKNOWN( 0, "Unknown" ),
    SUCCESS( 200, "Success" ),
    CREATED( 201, "Created" ),
    ACCEPTED( 202, "Accepted" ),
    NO_CONTENT( 203, "No Content" ),
    BAD_REQUEST( 400, "Bad Request" ),
    UNAUTHORIZED( 401, "Unauthorized" ),
    FORBIDDEN( 403, "Forbidden" ),
    NOT_FOUND( 404, "Not Found" ),
    METHOD_NOT_ALLOWED( 405, "Method Not Allowed" ),
    INTERNAL_SERVER_ERROR( 500, "Internal Server Error" ),
    UNAVAILABLE( 503, "Unavailable" );

    /**
     * The response code
     */
    private final int code;
    
    /**
     * The caption 
     */
    private final String caption;
    
    /**
     * A list of existing enum values 
     */
    private static ResponseCode[] values = null;
    
    
    
    /**
     * Create a response code instance.
     * If code is not found, this returns zero (UNKNOWN) as the code.
     * @param code
     * @return enum 
     */
    public static ResponseCode create( final int code )
    {
      if ( values == null )
        values = ResponseCode.values();
      
      for ( final ResponseCode c : values )
      {
        if ( c.getCode() == code )
        {
          return c;
        }
      }
      
      return ResponseCode.UNKNOWN;
    }
    
    
    /**
     * Create a new response code instance 
     * @param code
     * @param caption 
     */
    ResponseCode( final int code, final String caption )
    {
      this.code = code;
      this.caption = caption;
    }
    
    
    /**
     * Retrieve the response code 
     * @return code 
     */
    public int getCode()
    {
      return code;
    }
    
    
    /**
     * Retrieve the response code caption 
     * @return Caption 
     */
    public String getCaption()
    {
      return caption;
    }
  }
  
  /**
   * Log 
   */
  private static Log LOG = LogFactory.getLog( JetAPIResponse.class );
  
  
  /**
   * Create a JetAPIResponse object from an APIResponse object 
   * @param that Some response 
   * @return A JetAPIResponse
   * @throws JetException if the API returned an error response
   */
  public static JetAPIResponse createFromAPIResponse( final APIResponse that )
     throws JetException
  {
    if ( !( that instanceof APIResponse ))
      throw new IllegalArgumentException( "that must be an instance of APIResponse" );
    
    //..Check the errors; this can throw an exception.
    checkErrors( that );
    
    //..Return the response copied into a jet response 
    try {
      return copyFrom( that, JetAPIResponse.class );
    } catch( NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e ) {
      throw new JetException( "Failed to create an instance of JetAPIResponse.class.  Ensure constructor matches APIResponse" );
    }
  }
  
  
  /**
   * Create a new JetAPIResponse instance 
   * @param pv
   * @param status
   * @param headers 
   */
  public JetAPIResponse( final ProtocolVersion pv, final StatusLine status, final List<Header> headers )
  {
    super( pv, status, headers );
    
    
  }
  
  
  /**
   * Check for errors and return the json object from the response if any
   * @param res response
   * @return json or null
   * @throws JetException
   */
  public static final JsonObject checkErrors( APIResponse res ) throws JetException
  {
    String content = res.getResponseContent();
    JsonObject json = null;

    if ( content.startsWith( "{" ))
      json = res.fromJSON();

    if ( json != null )
      checkErrors( json );

    return json;
  }


  /**
   * Check the response body for errors
   * @param res JSON results
   * @throws JetException if there's an issue
   */
  public static final void checkErrors( final JsonObject res )
      throws JetException
  {
    if ( res.containsKey( "errors" ))
    {
      final JsonArray errors = res.getJsonArray( "errors" );
      ArrayList<String> messages = new ArrayList<>();

      for ( JsonValue error : errors )
      {
        messages.add( error.toString());
      }

      throw new JetException( messages );
    }
  }  
  
  
  /**
   * If the response was successful
   * @return is success
   */
  public boolean isOk()
  {
    return getStatusLine().getStatusCode() == ResponseCode.SUCCESS.getCode();
  }
  
  
  /**
   * If the response code was 201
   * @return 
   */
  public boolean isCreated()
  {
    return getStatusLine().getStatusCode() == ResponseCode.CREATED.getCode();
  }
  
  
  /**
   * Is the response code 202
   * @return is it?
   */
  public boolean isAccepted()
  {
    return getStatusLine().getStatusCode() == ResponseCode.ACCEPTED.getCode();
  }
  

  /**
   * Is the response code 203
   * @return is it?
   */
  public boolean isNoContent()
  {
    return getStatusLine().getStatusCode() == ResponseCode.NO_CONTENT.getCode();
  }
  
  /**
   * Is the response code 400
   * @return is it?
   */
  public boolean isBadRequest()
  {
    return getStatusLine().getStatusCode() == ResponseCode.BAD_REQUEST.getCode();
  }
  

  /**
   * Is the response code 401
   * @return is it?
   */
  public boolean isUnauthorized()
  {
    return getStatusLine().getStatusCode() == ResponseCode.UNAUTHORIZED.getCode();
  }
  
  
  /**
   * Is the response code 403
   * @return is it?
   */
  public boolean isForbidden()
  {
    return getStatusLine().getStatusCode() == ResponseCode.FORBIDDEN.getCode();
  }
  
  /**
   * Is the response code 404
   * @return is it?
   */
  public boolean isNotFound()
  {
    return getStatusLine().getStatusCode() == ResponseCode.NOT_FOUND.getCode();
  }
  
  /**
   * Is the response code 405
   * @return is it?
   */
  public boolean isMethodNotAllowed()
  {
    return getStatusLine().getStatusCode() == ResponseCode.METHOD_NOT_ALLOWED.getCode();
  }
  
  
  /**
   * Is the response code 500
   * @return is it?
   */
  public boolean isInternalServerError()
  {
    return getStatusLine().getStatusCode() == ResponseCode.INTERNAL_SERVER_ERROR.getCode();
  }
  
  
  /**
   * Is the response code 503
   * @return 
   */
  public boolean isUnavailable()
  {
    return getStatusLine().getStatusCode() == ResponseCode.UNAVAILABLE.getCode();
  }
  
}
