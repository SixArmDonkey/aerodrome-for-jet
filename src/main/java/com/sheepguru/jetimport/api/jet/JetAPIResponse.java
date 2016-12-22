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

package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APIResponse;
import com.sheepguru.jetimport.api.IAPIResponse;
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
public class JetAPIResponse extends APIResponse implements IJetAPIResponse
{

  /**
   * HTTP Response codes for the Jet API 
   * @see <a href="https://developer.jet.com/docs/jet-api-responses-and-errors#section-summary">
   * https://developer.jet.com/docs/jet-api-responses-and-errors#section-summary</a>
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
    private static final ResponseCode[] values = values();
    
    
    
    /**
     * Create a response code instance.
     * If code is not found, this returns zero (UNKNOWN) as the code.
     * @param code
     * @return enum 
     */
    public static ResponseCode create( final int code )
    {
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
  private static final Log LOG = LogFactory.getLog( JetAPIResponse.class );
  
  
  /**
   * Create a JetAPIResponse object from an IAPIResponse object 
   * @param that Some response 
   * @return A JetAPIResponse
   * @throws JetException if the API returned an error response
   */
  public static JetAPIResponse createFromAPIResponse( final IAPIResponse that )
     throws JetException
  {
    if ( !( that instanceof IAPIResponse ))
      throw new IllegalArgumentException( "that must be an instance of APIResponse" );
    
    //..Check the errors; this can throw an exception.
    checkErrors( that );
    
    //..Return the response copied into a jet response 
    try {
      return JetAPIResponse.copyFrom( that, JetAPIResponse.class );
    } catch( NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalArgumentException | IllegalAccessException e ) {
      throw new JetException( "Failed to create an instance of JetAPIResponse.class.  Ensure constructor matches APIResponse" );
    }
  }
  
  
  /**
   * Create a new JetIAPIResponse instance 
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
  public static final JsonObject checkErrors( IAPIResponse res ) throws JetException
  {
    String content = res.getResponseContent();
    JsonObject json = null;

    if ( content.startsWith( "{" ))
      json = res.getJsonObject();

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

      for ( final JsonValue error : errors )
      {
        messages.add( error.toString());
      }

      throw new JetException( messages );
    }
    else if ( res.containsKey( "error" ))
    {
      throw new JetException( res.getString( "error" ));
    }
  }  
  
  
  /**
   * If the response was successful
   * @return is success
   */
  @Override
  public boolean isOk()
  {
    return getStatusLine().getStatusCode() == ResponseCode.SUCCESS.getCode();
  }
  
  
  /**
   * If the response code was 201
   * @return 
   */
  @Override
  public boolean isCreated()
  {
    return getStatusLine().getStatusCode() == ResponseCode.CREATED.getCode();
  }
  
  
  /**
   * Is the response code 202
   * @return is it?
   */
  @Override
  public boolean isAccepted()
  {
    return getStatusLine().getStatusCode() == ResponseCode.ACCEPTED.getCode();
  }
  

  /**
   * Is the response code 203
   * @return is it?
   */
  @Override
  public boolean isNoContent()
  {
    return getStatusLine().getStatusCode() == ResponseCode.NO_CONTENT.getCode();
  }
  
  /**
   * Is the response code 400
   * @return is it?
   */
  @Override
  public boolean isBadRequest()
  {
    return getStatusLine().getStatusCode() == ResponseCode.BAD_REQUEST.getCode();
  }
  

  /**
   * Is the response code 401
   * @return is it?
   */
  @Override
  public boolean isUnauthorized()
  {
    return getStatusLine().getStatusCode() == ResponseCode.UNAUTHORIZED.getCode();
  }
  
  
  /**
   * Is the response code 403
   * @return is it?
   */
  @Override
  public boolean isForbidden()
  {
    return getStatusLine().getStatusCode() == ResponseCode.FORBIDDEN.getCode();
  }
  
  /**
   * Is the response code 404
   * @return is it?
   */
  @Override
  public boolean isNotFound()
  {
    return getStatusLine().getStatusCode() == ResponseCode.NOT_FOUND.getCode();
  }
  
  /**
   * Is the response code 405
   * @return is it?
   */
  @Override
  public boolean isMethodNotAllowed()
  {
    return getStatusLine().getStatusCode() == ResponseCode.METHOD_NOT_ALLOWED.getCode();
  }
  
  
  /**
   * Is the response code 500
   * @return is it?
   */
  @Override
  public boolean isInternalServerError()
  {
    return getStatusLine().getStatusCode() == ResponseCode.INTERNAL_SERVER_ERROR.getCode();
  }
  
  
  /**
   * Is the response code 503
   * @return 
   */
  @Override
  public boolean isUnavailable()
  {
    return getStatusLine().getStatusCode() == ResponseCode.UNAVAILABLE.getCode();
  }
  
}
