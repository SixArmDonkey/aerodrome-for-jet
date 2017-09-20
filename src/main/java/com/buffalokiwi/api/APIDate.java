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

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A wrapper for all of the various date/time formats used within some API.
 * 
 * A set of base formats have been included in the base configuration.
 * 
 * Formats:
 * yyyy-MM-dd'T'HH:mm:ss
 * yyyy-MM-dd'T'HH:mm:ss'Z'
 * yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'
 * yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX
 * 
 * @author John Quinn
 */
public class APIDate implements IAPIDate 
{
  /**
   * Local time without timezone.
   * If not specified, this will assume the offset to be ZoneId.systemDefault()
   */
  public static final String FMT_LOCAL = "yyyy-MM-dd'T'HH:mm:ss";
  
  /**
   * UTC 
   */
  public static final String FMT_ZULU = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  
  /**
   * UTC with microseconds
   */
  public static final String FMT_ZULU_MICRO = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'";
  
  /**
   * UTC with forced zeroed microseconds
   */
  public static final String FMT_ZULU_ZERO = "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z";
  
  /**
   * Local date/time with microseconds and timezone offset 
   */
  public static final String FMT_LOCAL_MICRO = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";
       
  /**
   * Available formats for the parser to try
   */
  protected static final List<DateTimeFormatter> FORMATS = new CopyOnWriteArrayList<>();
  
  /**
   * Logger 
   */
  protected static final Log LOG = LogFactory.getLog( APIDate.class );
  
  /**
   * The zoned date and time to UTC
   */
  protected final ZonedDateTime date;
  
  /**
   * The zone offset 
   */
  protected final ZoneOffset offset;
  
  
  /**
   * Initialize the date/time formats used by Jet 
   */
  static {
    FORMATS.add( DateTimeFormatter.ISO_ZONED_DATE_TIME );
    FORMATS.add( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
    FORMATS.add( DateTimeFormatter.ISO_DATE_TIME );
    FORMATS.add(  new DateTimeFormatterBuilder()
      .appendPattern( FMT_LOCAL )
      .appendFraction( ChronoField.MICRO_OF_SECOND, 0, 9, true )
      .appendOffset( "+HH:MM", "Z" )
      .toFormatter());
    FORMATS.add( new DateTimeFormatterBuilder()
      .appendPattern( FMT_LOCAL )
      .appendFraction( ChronoField.MICRO_OF_SECOND, 0, 9, true )
      .appendOffset( "+HHMM", "+0000" )
      .toFormatter());
    FORMATS.add( new DateTimeFormatterBuilder()
      .appendPattern( FMT_LOCAL )
      .toFormatter());
  }
  
  
  /**
   * Add a custom date/time format to the list 
   * @param format Format to add
   */
  public void addFormat( final DateTimeFormatter format )
  {
    if ( format == null )
      throw new IllegalArgumentException( "format can't be null" );
    
    if ( !FORMATS.contains( format ))
      FORMATS.add( format );
  }

  
  /**
   * Attempt to take some value and turn it into a valid APIDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static APIDate fromStringOrNull( String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    for ( final DateTimeFormatter fmt : FORMATS )
    {
      try {
        final TemporalAccessor t = fmt.parse( value );
        
        try {
          return new APIDate( ZonedDateTime.from( t ));
        } catch( DateTimeException e ) {
          APILog.warn( LOG, e, "Failed to determine timezone.  Defaulting to local offset" );
          final LocalDateTime local = LocalDateTime.from( t );
          final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset( Instant.now());
          return new APIDate( ZonedDateTime.of( local, offset ));
        }
      } catch( DateTimeParseException e ) {
        //..do nothing, yet.
      } 
    }
    
    //..Not found.  Log it and return null
    APILog.error(  LOG, "Failed to parse date string:", value );
    return null;
  }
  
  
  /**
   * Create a new APIDate instance from ZonedDateTime 
   * @param zdt 
   */
  public APIDate( final ZonedDateTime zdt )
  {
    if ( zdt == null )
      throw new IllegalArgumentException( "zdt can't be null" );
    
    date = zdt;
    
    this.offset = zdt.getOffset();
  }
  
  
  /**
   * Create a new APIDate set to now.
   * This will use the local system offset
   */
  public APIDate()
  {
    this( Instant.now().atZone( ZoneId.systemDefault()));
  }
  
  
  /**
   * Create a new APIDate using the local system offset 
   * @param date date to use 
   */
  public APIDate( final Date date )
  {
    if ( date == null )
      throw new IllegalArgumentException( "date can't be null" );
    
    this.date = date.toInstant().atZone( ZoneId.systemDefault());
    offset = ZoneId.systemDefault().getRules().getOffset( date.toInstant());
  }
  
  
  /**
   * Retrieve the date in the local time zone
   * @return zoned time
   */
  @Override
  public ZonedDateTime getLocalDate()
  {
    return date.withZoneSameInstant( ZoneId.systemDefault());
  }

  
  /**
   * Retrieve the exact string retrieved from the api response that 
   * represents a date.
   * @param pattern DateTimeFormatter pattern to use for ofPattern()
   * @return date string 
   */
  @Override
  public String getDateString( final String pattern )
  {
    return date.format( DateTimeFormatter.ofPattern( pattern ));
  }
  
  
  /**
   * Retrieve the local date as a string.
   * This does NOT include zone information
   * @return date/time
   */
  @Override
  public String getLocalDateString()
  {
    return date.withZoneSameInstant( offset ).format(  DateTimeFormatter.ofPattern( FMT_LOCAL ));
  }  
  
  
  /**
   * Retrieve the date in UTC.
   * @return  Date
   */
  @Override
  public Date getDate()
  {
    return Date.from( date.toInstant());
  }
  
  
  /**
   * Convert whatever date this is to the system zone
   * @return system date/time
   */
  @Override
  public Date toLocalDate()
  {
    return Date.from( date.withZoneSameInstant( ZoneId.systemDefault()).toInstant());
  }
  
  
  /**
   * Converts the internal UTC date/time to a timestamp.
   * The returned Timestamp will always be UTC.
   * @return timestamp
   */
  @Override
  public Timestamp toSqlTimestamp()
  {
    return new Timestamp( getDate().getTime());
  }
  
  
  @Override
  public String toString()
  {
    return getDateString( FMT_ZULU_MICRO );
  }
}
