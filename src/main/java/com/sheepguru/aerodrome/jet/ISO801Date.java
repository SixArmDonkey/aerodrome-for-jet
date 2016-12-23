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

package com.sheepguru.aerodrome.jet;

import java.util.Date;


/**
 * A JetDate with a time zone offset. ISO-8601  
 * Some stuff wants this type of date, Jet is inconsistent.
 * 
 * @author john
 */
public class ISO801Date extends ISO801UTCDate 
{
  private static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"; 

  
  
  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static ISO801Date fromJetValueOrNull( final String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    return new ISO801Date( value );
  }
  
  
  public ISO801Date()
  {
    super( new Date(), TZ_FORMAT );
  }
  
  /**
   * Create a new JetDate
   * @param date Date string
   */
  public ISO801Date( final String date )
  {
    super( date, TZ_FORMAT );
  }
  
  
  /**
   * Create a new JetDate
   * @param date date to use 
   */
  public ISO801Date( final Date date )
  {
    super( date, TZ_FORMAT );
  }  
}
