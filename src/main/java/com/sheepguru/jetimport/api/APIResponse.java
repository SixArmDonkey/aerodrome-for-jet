
package com.sheepguru.jetimport.api;

import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * The response object used to store data related to a HTTP request response.
 */
public class APIResponse
{
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


  /**
   * Create a new Response instance.
   * This must contain the response from some http request
   * @param pv protocol version
   * @param status status line
   * @param headers response headers
   */
  APIResponse( final ProtocolVersion pv, final StatusLine status, final List<Header> headers )
  {
    protocolVersion = pv;
    this.status = status;
    this.headers = headers;
    processHeaders();
  }


  /**
   * Retrieve the protocol version
   * @return version
   */
  public ProtocolVersion getProtocolVersion()
  {
    return protocolVersion;
  }


  /**
   * Retrieve the status line
   * @return status
   */
  public StatusLine getStatusLine()
  {
    return status;
  }


  /**
   * Access the response headers list
   * @return headers
   */
  public List<Header> headers()
  {
    return headers;
  }


  /**
   * Retrieve the content length
   * @return length
   */
  public int getContentLength()
  {
    return contentLength;
  }


  /**
   * Set the HTTP response content and character set.
   * @param content Content
   * @param charset character set name
   */
  public void setContent( final String content, final String charset )
  {
    this.content = content;
    this.charset = charset;
  }


  /**
   * Retrieve the response content
   * @return content
   */
  public String getResponseContent()
  {
    return content;
  }


  /**
   * Retrieve the response content character set name
   * @return charset name
   */
  public String getResponseCharsetName()
  {
    return charset;
  }


  /**
   * Retrieve the response as a parsed JsonObject
   * @return response
   */
  public JsonObject fromJSON()
  {
    try ( JsonReader reader = Json.createReader( new StringReader( content ))) {
      return reader.readObject();
    }
  }


  /**
   * Find out if the last request was successful
   * @return request successful
   */
  public boolean isSuccess()
  {
    return status.getStatusCode() >= 200 && status.getStatusCode() < 300;
  }


  /**
   * Find out if the last request was a failure
   * Code is 400-599
   * @return is fail
   */
  public boolean isFailure()
  {
    return status.getStatusCode() >= 400 && status.getStatusCode() < 600;
  }


  /**
   * Find out if the last request was a failure due to client input.
   * Code: 400-499
   * @return is fail
   */
  public boolean isRequestFail()
  {
    return status.getStatusCode() >= 400 && status.getStatusCode() < 500;
  }


  /**
   * Find out if the last request returned a 500 range error.
   * @return is server failure 
   */
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