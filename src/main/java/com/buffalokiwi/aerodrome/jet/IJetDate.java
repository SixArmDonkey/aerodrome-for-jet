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

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * A jet date 
 * @todo Remove all of this shit and replace it with the java 8 java.time stuff.
 * @author John Quinn
 */
public interface IJetDate 
{

  /**
   * Retrieve the date.
   * Note: this can be incorrect if the formatter failed.
   * @return  Date
   */
  public Date getDate();

  /**
   * Retrieve the exact string retrieved from the jet api response that 
   * represents a date.
   * @return date string 
   */
  public String getDateString( final String pattern ); 
  
  
  /**
   * Retrieve the local date as a string.
   * This does NOT include zone information
   * @return date/time
   */
  public String getLocalDateString();
  
  /**
   * Convert this date into some sql representation.
   * This effectively drops timezone information.
   * @return timestamp
   */
  public Timestamp toSqlTimestamp();
  
  /**
   * Retrieve the jet date in the local time zone
   * @return zoned time
   */
  public ZonedDateTime getLocalDate();  
  
  /**
   * Convert whatever date this is to the system zone
   * @return system date/time
   */
  public Date toLocalDate();  
}
