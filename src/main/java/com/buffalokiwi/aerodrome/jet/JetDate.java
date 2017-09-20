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

import com.buffalokiwi.api.APIDate;
import com.buffalokiwi.api.APILog;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * A wrapper for all of the various date/time formats used within the Jet API.
 * 
 * A set of base formats have been included in the base configuration.
 * 
 * Formats:
 * yyyy-MM-dd'T'HH:mm:ss
 * yyyy-MM-dd'T'HH:mm:ss'Z'
 * yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'
 * yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX
 * 
 * 
 * @todo Remove all of this shit and replace it with the java 8 java.time stuff.
 * 
 * @author John Quinn
 */
public class JetDate extends APIDate implements IJetDate 
{
  /**
   * A standard American style date
   */
  public static final String FMT_STD = "MM/dd/yyyy";
  
  
  /**
   * Initialize the date/time formats used by Jet 
   */
  static {    
    FORMATS.add( new DateTimeFormatterBuilder()
      .appendPattern( FMT_STD )
      .toFormatter());
  }
  
 
  
  private static TemporalAccessor parseDate( final DateTimeFormatter fmt, final String value )
  {
    //..Check for fmt_std by value length
    if ( value.trim().length() < 11 )
    {
      LocalDate d = LocalDate.parse( value, fmt );
      return d.atStartOfDay( ZoneId.systemDefault());
    }
    
    return fmt.parse( value );
  }

  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static JetDate fromJetValueOrNull( String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    for ( final DateTimeFormatter fmt : FORMATS )
    {
      try {
        final TemporalAccessor t = parseDate( fmt, value );
        try {
          return new JetDate( ZonedDateTime.from( t ));
        } catch( DateTimeException e ) {
          APILog.warn( LOG, e, "Failed to determine timezone.  Defaulting to local offset" );
          final LocalDateTime local = LocalDateTime.from( t );
          final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset( Instant.now());
          return new JetDate( ZonedDateTime.of( local, offset ));
        }
      } catch( DateTimeParseException e ) {
        //..do nothing, yet.
      } 
    }
    
    
    
    
    
    //..Not found.  Log it and return null
    APILog.error(  LOG, "Failed to parse date string:", value );
    return null;
  }

  
  
  public JetDate( final ZonedDateTime zdt )
  {
    super( zdt );
  }
  
  
  
  /**
   * Create a new JetDate set to now.
   * This will use the local system offset
   */
  public JetDate()
  {
    super();
  }
  
  
  /**
   * Create a new JetDate using the local system offset 
   * @param date date to use 
   */
  public JetDate( final Date date )
  {
    super( date );
  }
}
