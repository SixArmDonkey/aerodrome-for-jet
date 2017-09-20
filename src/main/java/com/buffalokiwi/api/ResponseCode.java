/**
 * This file is part of the MagentoAPI package, and is subject to the 
 * terms and conditions defined in file 'LICENSE.txt', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.buffalokiwi.api;

/**
 * Some common response codes used with API's 
 * @author John Quinn
 */
public enum ResponseCode 
{
  UNKNOWN( 0, "Unknown" ),
  SUCCESS( 200, "Success" ),
  CREATED( 201, "Created" ),
  ACCEPTED( 202, "Accepted" ),
  NO_CONTENT( 203, "No Content" ),
  MULTI_STATUS( 207, "Multi Status" ),
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
