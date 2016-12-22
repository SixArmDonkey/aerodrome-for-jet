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

import java.util.Date;


/**
 * A JetDate with a time zone offset.  Some stuff wants this type of date, 
 * other stuff wants it with just a 'Z'.  Jet is inconsistent.
 * @author john
 */
public class JetDateWithOffset extends JetDate 
{
  private static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"; 

  
  
  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static JetDateWithOffset fromJetValueOrNull( final String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    return new JetDateWithOffset( value );
  }
  
  
  public JetDateWithOffset()
  {
    super( new Date(), TZ_FORMAT );
  }
  
  /**
   * Create a new JetDate
   * @param date Date string
   */
  public JetDateWithOffset( final String date )
  {
    super( date, TZ_FORMAT );
  }
  
  
  /**
   * Create a new JetDate
   * @param date date to use 
   */
  public JetDateWithOffset( final Date date )
  {
    super( date, TZ_FORMAT );
  }  
}
