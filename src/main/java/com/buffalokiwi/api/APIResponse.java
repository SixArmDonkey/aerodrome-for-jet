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

package com.buffalokiwi.api;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * The response object used to store data related to a HTTP request response.
 */
public class APIResponse implements IAPIResponse
{
  private static final Log LOG = LogFactory.getLog( APIResponse.class );
  
  /**
   * APIResponse protocol version
   */
  private final ProtocolVersion protocolVersion;

  /**
   * APIResponse status line
   */
  private final StatusLine status;

  /**
   * APIResponse hedaers
   */
  private final List<Header> headers;
  
  /**
   * The 301/302 redirect chain
   */
  private final List<URI> redirectLocations;

  /**
   * APIResponse content length
   */
  private int contentLength = 0;

  /**
   * APIResponse content
   */
  private String content = "";

  /**
   * APIResponse charset
   */
  private String charset = "";
  
  private byte[] bytes;
  
  
  /**
   * Clone an api response 
   * @param <T> some class that extends APIResponse
   * @param that Some response to clone 
   * @param type The class 
   * @return A copy of that 
   * @throws java.lang.NoSuchMethodException 
   * @throws java.lang.InstantiationException 
   * @throws java.lang.reflect.InvocationTargetException 
   * @throws java.lang.IllegalAccessException 
   */
  public static <T extends APIResponse> T copyFrom( final IAPIResponse that, Class<T> type ) 
    throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalArgumentException, IllegalAccessException 
  {    
    for ( final Constructor<?> c: type.getConstructors())
    {
      if ( c.getParameterCount() == 6 )
      {
        final T r = (T)c.newInstance( that.getProtocolVersion(), that.getStatusLine(), that.headers(), that.getRedirectLocations(), that.getBytes(), that.getResponseCharsetName());
        return r;
      }
    }
    //T r = type.getConstructor( ProtocolVersion.class, StatusLine.class, List.class ).newInstance( that.getProtocolVersion(), that.getStatusLine(), that.headers());
    //r.setContent( that.getResponseContent(), that.getResponseCharsetName());
    //return r;    
    throw new NoSuchMethodException( "Failed to locate constructor in class " + type );
  }
  
  
  /**
   * Create a new Response instance.
   * This must contain the response from some http request
   * @param pv protocol version
   * @param status status line
   * @param headers response headers
   */
  public APIResponse( final ProtocolVersion pv, final StatusLine status, final List<Header> headers, final List<URI> redirectLocations, final byte[] content, final String charset )
  {
    protocolVersion = pv;
    this.status = status;
    
    if ( headers != null )
      this.headers = Collections.unmodifiableList( headers );
    else
      this.headers = Collections.unmodifiableList( new ArrayList<>());
    
    if ( redirectLocations != null )
      this.redirectLocations = Collections.unmodifiableList( redirectLocations );
    else
      this.redirectLocations = Collections.unmodifiableList( new ArrayList<>());
    
    this.charset = ( charset == null) ? "" : charset;
    
    if ( !this.charset.isEmpty())
    {
      try {
        this.content = ( content == null ) ? "" : new String( content, 0, content.length, charset );
      } catch( UnsupportedEncodingException e ) {
        APILog.error( LOG, e, "Failed to create response string" );
        this.content = "";
      }
      bytes = new byte[0];
    }
    else
      this.bytes = content;
    
    processHeaders();
  }  
  

  /**
   * Create a new Response instance.
   * This must contain the response from some http request
   * @param pv protocol version
   * @param status status line
   * @param headers response headers
   */
  public APIResponse( final ProtocolVersion pv, final StatusLine status, final List<Header> headers, final byte[] content, final String charset )
  {
    this( pv, status, headers, null, content, charset );
  }
  
  
  @Override
  public byte[] getBytes()
  {    
    if ( !content.isEmpty())
    {
      try {
        return content.getBytes( charset );
      } catch( UnsupportedEncodingException e ) {
        return content.getBytes();
      }
    }
    else
      return bytes;
  }


  /**
   * Retrieve the protocol version
   * @return version
   */
  @Override
  public ProtocolVersion getProtocolVersion()
  {
    return protocolVersion;
  }


  /**
   * Retrieve the status line
   * @return status
   */
  @Override
  public StatusLine getStatusLine()
  {
    return status;
  }


  /**
   * Access the response headers list
   * @return headers
   */
  @Override
  public List<Header> headers()
  {
    return headers;
  }
  
  
  /**
   * Retrieve the redirect chain
   * @return 301/302 redirects as part of this request 
   */
  @Override
  public List<URI> getRedirectLocations()
  {
    return redirectLocations;
  }


  /**
   * Retrieve the content length
   * @return length
   */
  @Override
  public int getContentLength()
  {
    return contentLength;
  }


  /**
   * Retrieve the response content
   * @return content
   */
  @Override
  public String getResponseContent()
  {
    return content;
  }


  /**
   * Retrieve the response content character set name
   * @return charset name
   */
  @Override
  public String getResponseCharsetName()
  {
    return charset;
  }


  /**
   * Retrieve the response as a parsed JsonObject
   * @return response
   * @throws JsonException if a JSON object cannot
   *     be created due to i/o error (IOException would be
   *     cause of JsonException)
   * @throws javax.json.stream.JsonParsingException if a JSON object cannot
   *     be created due to incorrect representation
   */
  @Override
  public JsonObject getJsonObject()
    throws JsonException, JsonParsingException
  {
    try ( final JsonReader reader = Json.createReader( 
      new StringReader( content ))) 
    {
      return reader.readObject();
    }
  }

  
  /**
   * A crude way to see if a response might have json in it.
   * Checks to see if a { is in position 0.
   * 
   * @return might be json
   */
  @Override
  public boolean isJson()
  {
    return content.trim().startsWith( "{" );
  }
  

  /**
   * Find out if the last request was successful
   * @return request successful
   */
  @Override
  public boolean isSuccess()
  {
    return status.getStatusCode() >= 200 && status.getStatusCode() < 300;
  }


  /**
   * Find out if the last request was a failure
   * Code is 400-599
   * @return is fail
   */
  @Override
  public boolean isFailure()
  {
    return status.getStatusCode() >= 400 && status.getStatusCode() < 600;
  }


  /**
   * Find out if the last request was a failure due to client input.
   * Code: 400-499
   * @return is fail
   */
  @Override
  public boolean isRequestFail()
  {
    return status.getStatusCode() >= 400 && status.getStatusCode() < 500;
  }


  /**
   * Find out if the last request returned a 500 range error.
   * @return is server failure 
   */
  @Override
  public boolean isServerFailure()
  {
    return status.getStatusCode() >= 500 && status.getStatusCode() < 600;
  }


  /**
   * Process the headers to look for stuff like content length
   */
  private void processHeaders()
  {
    //..Check for a content length header
    for ( Header h : headers )
    {
      if ( h.getName().equals( "Content-Length" ))
      {
        try {
          contentLength = Integer.valueOf( h.getValue());
        } catch( NumberFormatException e ) {
          contentLength = -1;
        }
      }
    }
  }
}