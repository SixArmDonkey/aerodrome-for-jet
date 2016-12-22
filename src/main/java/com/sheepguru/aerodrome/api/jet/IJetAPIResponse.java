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
package com.sheepguru.aerodrome.api.jet;

import com.sheepguru.aerodrome.api.IAPIResponse;


/**
 * Represents an API response from Jet 
 * @author John Quinn
 */
public interface IJetAPIResponse extends IAPIResponse
{

  /**
   * Is the response code 202
   * @return is it?
   */
  public boolean isAccepted();

  /**
   * Is the response code 400
   * @return is it?
   */
  public boolean isBadRequest();

  /**
   * If the response code was 201
   * @return
   */
  public boolean isCreated();

  /**
   * Is the response code 403
   * @return is it?
   */
  public boolean isForbidden();

  /**
   * Is the response code 500
   * @return is it?
   */
  public boolean isInternalServerError();

  /**
   * Is the response code 405
   * @return is it?
   */
  public boolean isMethodNotAllowed();

  /**
   * Is the response code 203
   * @return is it?
   */
  public boolean isNoContent();

  /**
   * Is the response code 404
   * @return is it?
   */
  public boolean isNotFound();

  /**
   * If the response was successful
   * @return is success
   */
  public boolean isOk();

  /**
   * Is the response code 401
   * @return is it?
   */
  public boolean isUnauthorized();

  /**
   * Is the response code 503
   * @return
   */
  public boolean isUnavailable();
  
}
