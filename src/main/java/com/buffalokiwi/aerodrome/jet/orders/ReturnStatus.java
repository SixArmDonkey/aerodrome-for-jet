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

package com.buffalokiwi.aerodrome.jet.orders;

/**
 * To check for returns you will the utilize the Get Returns method. Please 
 * replace {status} with one of the following values:
 *
 *   created
 *   acknowledge - no longer in use
 *   inprogress
 *   completed by merchant
 * This will return a list of returns in that status.
 * 
 * @author John Quinn
 */
public enum ReturnStatus 
{
  /**
   * Not specified 
   */
  NONE( "" ),
  
  /**
   * Created
   */
  CREATED( "created" ),
  
  /**
   * In progress
   */
  IN_PROGRESS( "inprogress" ),
  
  /**
   * Completed
   */
  COMPLETED( "completed by merchant" );
  
  /**
   * Enum values 
   */
  private static final ReturnStatus[] values = values();
  
  
  /**
   * Attempt to create a ReturnStatus by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ReturnStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ReturnStatus c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  
  /**
   * Create a new ReturnStatus enum 
   * @param text jet text
   */
  ReturnStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text 
   * @return jet text
   */
  public String getText()
  {
    return text;
  }
}
