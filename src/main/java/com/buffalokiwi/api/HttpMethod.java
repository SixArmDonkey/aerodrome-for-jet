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
 * Simple lil http method class.
 * @author John Quinn
 */
public enum HttpMethod 
{
  //..Methods 
  GET( "GET" ), 
  POST( "POST" ), 
  PUT( "PUT" ),
  DELETE( "DELETE" ),
  HEAD( "HEAD" ),
  OPTIONS( "OPTIONS" );
  
  /**
   * Method as a string
   */
  private final String text;
  
  /**
   * Constructor
   * @param text Method as a string
   */
  HttpMethod( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the method as a string 
   * @return method 
   */
  public String getText()
  {
    return text;
  }
}
