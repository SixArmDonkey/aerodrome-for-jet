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

package com.sheepguru.api;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Represents an API Http Client 
 * @author john
 */
public interface IAPIHttpClient 
{
  /**
   * Create a new HttpClient instance to use
   * @return
   * @throws IllegalArgumentException
   * @throws APIException If there is a problem creating the client or strategy
   */
  public CloseableHttpClient createNewClient() throws IllegalArgumentException, 
    APIException;

  /**
   * Retrieve the accept header value
   * @return accept header value
   */
  public String getAccept();

  /**
   * Retrieve the Accept-Language header value
   * @return value
   */
  public String getAcceptLanguages();

  /**
   * Retrieve a shared client instance to use
   * @return client
   */
  public CloseableHttpClient getClient();

  /**
   * Retrieve the crawl delay
   * @return millis
   */
  public long getCrawlDelay();

  /**
   * Retrieve the host
   * @return host
   */
  public URIBuilder getHost();

  /**
   * Retrieve the current socket read timeout value
   * @return milliseconds
   */
  public long getReadTimeout();

  /**
   * Retrieve the user agent
   * @return User agent
   */
  public String getUserAgent();

  /**
   * Find out if gzip is allowed or not
   * @return can use gzip
   */
  public boolean isGzipAllowed();
  
}
