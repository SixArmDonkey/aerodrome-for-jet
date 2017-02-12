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

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;

/**
 * Interface for the core API Library 
 * @author John Quinn
 */
public interface IApi 
{
  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @return The response
   * @throws APIException If something goes wrong
   */
  public IAPIResponse get(final String url) throws APIException;

  /**
   * Perform a get-based request to some endpoint
   * @param url The URL
   * @param headers Extra headers to send
   * @return The response
   * @throws APIException If something goes wrong
   */
  public IAPIResponse get(final String url, final Map<String, String> headers) 
    throws APIException;

  
  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @return response
   * @throws APIException If something goes wrong
   */
  public IAPIResponse post(final String url, final List<NameValuePair> formData) 
    throws APIException;

  
  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @param files Key/File files to send
   * @return response
   * @throws APIException If something goes wrong
   */
  public IAPIResponse post(final String url, final List<NameValuePair> formData, 
    final Map<String, PostFile> files) throws APIException;

  
  /**
   * Perform a post-based request to some endpoint
   * @param url The URL
   * @param formData Key/Value pairs to send
   * @param files Key/File files to send
   * @param headers Extra headers to send
   * @return response
   * @throws APIException If something goes wrong
   */
  public IAPIResponse post(final String url, final List<NameValuePair> formData, 
    final Map<String, PostFile> files, final Map<String, String> headers) 
    throws APIException;

  
  /**
   * Send arbitrary post data to some endpoint
   * @param url URL
   * @param payload Data to send
   * @return response
   * @throws APIException if something goes wrong
   */
  public IAPIResponse post(final String url, final String payload) 
    throws APIException;

  
  /**
   * Send arbitrary post data to some endpoint
   * @param url URL
   * @param payload Data to send
   * @param headers Extra headers to send
   * @return response
   * @throws APIException if something goes wrong
   */
  public IAPIResponse post(final String url, final String payload, 
    final Map<String, String> headers) throws APIException;

  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  public IAPIResponse post(final String url, final InputStream payload, 
    final long contentLength, final ContentType contentType, 
    final Map<String, String> headers) throws APIException;

  public IAPIResponse post(final String url, final PostFile file, 
    Map<String, String> headers) throws APIException;

  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @return response
   * @throws APIException
   */
  public IAPIResponse put(final String url, final String payload) 
    throws APIException;

  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  public IAPIResponse put(final String url, final String payload, 
    final Map<String, String> headers) throws APIException;

  
  /**
   * Perform a put-based request to some endpoint
   * @param url URL
   * @param payload Payload to send
   * @param headers additional headers to send
   * @return response
   * @throws APIException
   */
  public IAPIResponse put(final String url, final InputStream payload, 
    final long contentLength, final ContentType contentType, 
    final Map<String, String> headers) throws APIException;

  
  public IAPIResponse put(final String url, final PostFile file, 
    Map<String, String> headers) throws APIException;
  
}
