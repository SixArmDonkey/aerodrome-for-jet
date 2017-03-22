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

import java.util.Date;


/**
 * A JetDate with a time zone offset. ISO-8601  
 * Some stuff wants this type of date, Jet is inconsistent.
 * 
 * Uses format: yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX
 * 
 * @todo Remove all of this shit and replace it with the java 8 java.time stuff.
 * 
 * @author john
 */
public class ISO8601Date extends ISO8601UTCDate 
{
  private static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"; 

  
  
  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static ISO8601Date fromJetValueOrNull( final String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    return new ISO8601Date( value );
  }
  
  
  public ISO8601Date()
  {
    super( new Date(), TZ_FORMAT );
  }
  
  /**
   * Create a new JetDate
   * @param date Date string
   */
  public ISO8601Date( final String date )
  {
    super( date, TZ_FORMAT );
  }
  
  
  /**
   * Create a new JetDate
   * @param date date to use 
   */
  public ISO8601Date( final Date date )
  {
    super( date, TZ_FORMAT );
  }  
}
