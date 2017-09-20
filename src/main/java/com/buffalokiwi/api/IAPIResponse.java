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

import java.net.URI;
import java.util.List;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * Represents a response from some API 
 * @author John Quinn
 */
public interface IAPIResponse 
{
  /**
   * Retrieve the content length
   * @return length
   */
  public int getContentLength();

  /**
   * Retrieve the response as a parsed JsonObject
   * @return response
   * @throws JsonException if a JSON object cannot
   *     be created due to i/o error (IOException would be
   *     cause of JsonException)
   * @throws javax.json.stream.JsonParsingException if a JSON object cannot
   *     be created due to incorrect representation
   */
  public JsonObject getJsonObject() throws JsonException, JsonParsingException;

  /**
   * Retrieve the protocol version
   * @return version
   */
  public ProtocolVersion getProtocolVersion();

  /**
   * Retrieve the response content character set name
   * @return charset name
   */
  public String getResponseCharsetName();

  /**
   * Retrieve the response content
   * @return content
   */
  public String getResponseContent();

  /**
   * Retrieve the status line
   * @return status
   */
  public StatusLine getStatusLine();

  /**
   * Access the response headers list
   * @return headers
   */
  public List<Header> headers();
  
  /**
   * Retrieve the redirect chain
   * @return 301/302 redirects as part of this request 
   */
  public List<URI> getRedirectLocations();
  

  /**
   * Find out if the last request was a failure
   * Code is 400-599
   * @return is fail
   */
  public boolean isFailure();

  /**
   * A crude way to see if a response might have json in it.
   * Checks to see if a { is in position 0.
   *
   * @return might be json
   */
  public boolean isJson();

  /**
   * Find out if the last request was a failure due to client input.
   * Code: 400-499
   * @return is fail
   */
  public boolean isRequestFail();

  /**
   * Find out if the last request returned a 500 range error.
   * @return is server failure
   */
  public boolean isServerFailure();

  /**
   * Find out if the last request was successful
   * @return request successful
   */
  public boolean isSuccess();
}
