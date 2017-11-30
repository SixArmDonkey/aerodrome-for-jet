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

package com.buffalokiwi.aerodrome.jet;

import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIResponse;
import com.buffalokiwi.api.IApi;
import com.buffalokiwi.api.PostFile;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.http.entity.ContentType;

/**
 * Represents the JetAPI base class
 * @author John Quinn
 * 
 * @todo Messy Messy....
 */
public interface IJetAPI extends IApi
{

  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong (like an IOException)
   * @throws JetException if jet has some issue 
   */
  @Override
  public IJetAPIResponse get(final String url, final Map<String, String> headers) 
          throws APIException, JetException;

  /**
   * Retrieve a HeaderBuilder instance with an Authorization header
   * @return builder
   */
  public JetHeaderBuilder getHeaderBuilder();

  /**
   * Retrieve a headers map for use with a JSON request
   * @return JSON builder
   */
  public JetHeaderBuilder getJSONHeaderBuilder();

  /**
   * Retrieve a headers map for use with a plain text request
   * @return plain text builder
   */
  public JetHeaderBuilder getPlainHeaderBuilder();
  
  
  /**
   * Add an error handler callback.
   * @param handler 
   */
  public void setErrorHandler( IJetErrorHandler handler );
          
  /**
   * Add a rate limit handler 
   * @param handler 
   */
  public void setRateLimitHandler( final Consumer<IAPIResponse> handler );

  /**
   * Send arbitrary post data to some endpoint
   * @param url URL
   * @param payload Data to send
   * @param headers Extra headers to send
   * @return response
   * @throws APIException if something goes wrong
   */
  @Override
  public IJetAPIResponse post(final String url, final String payload, 
          final Map<String, String> headers) throws APIException, JetException;

  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse post(final String url, final InputStream payload, 
          final long contentLength, final ContentType contentType, 
          final Map<String, String> headers) throws APIException;

  @Override
  public IJetAPIResponse post(final String url, final PostFile file, 
          Map<String, String> headers) throws APIException;

  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse put(final String url, final String payload, 
          final Map<String, String> headers) throws APIException, JetException;

  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  @Override
  public IJetAPIResponse put(final String url, final InputStream payload, 
          final long contentLength, final ContentType contentType, 
          final Map<String, String> headers) throws APIException, JetException;

  @Override
  public IJetAPIResponse put(final String url, final PostFile file, 
          Map<String, String> headers) throws APIException, JetException;
  
}
