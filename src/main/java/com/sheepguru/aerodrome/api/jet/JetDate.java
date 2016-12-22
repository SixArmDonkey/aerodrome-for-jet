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

import com.sheepguru.aerodrome.api.APILog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a Date from a Jet API Response.
 * Dates 
 * @author John Quinn
 */
public class JetDate implements IJetDate 
{  
  /**
   * Default date format 
   */
  private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'";  
 
  /**
   * Logger 
   */
  private static final Log LOG = LogFactory.getLog( JetDate.class );
  
  /**
   * Date string from jet 
   */
  private final String date;
  
  /**
   * The data date or the current date if it fails to format 
   */
  private final Date javaDate;

  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static JetDate fromJetValueOrNull( final String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    try {
      return new JetDate( new SimpleDateFormat( 
        DEFAULT_FORMAT, Locale.ENGLISH ).parse( value ));
    } catch( ParseException e ) {
      APILog.error( LOG, e, "Failed to parse date", value, "with format", 
        DEFAULT_FORMAT );
      return null;
    }
  }
  
  
  /**
   * Create a new JetDate set to now 
   */
  public JetDate()
  {
    this( new Date());
  }
  
  
  /**
   * Create a new JetDate
   * @param date Date string
   */
  public JetDate( final String date )
  {
    this( date, DEFAULT_FORMAT );
  }
  
  
  /**
   * Create a new JetDate
   * @param date date to use 
   */
  public JetDate( final Date date )
  {
    Utils.checkNull( date, "date" );
    
    this.javaDate = new Date( date.getTime());
    this.date = new SimpleDateFormat( DEFAULT_FORMAT, Locale.ENGLISH ).format( javaDate );    
  }


  /**
   * Create a new JetDate
   * @param date date to use 
   * @param format format string
   */
  public JetDate( final Date date, final String format )
  {
    Utils.checkNull( date, "date" );
    
    this.javaDate = new Date( date.getTime());
    this.date = new SimpleDateFormat( format, Locale.ENGLISH ).format( javaDate );    
  }
  
  
  
  /**
   * Create a new JetDate
   * @param date Date
   * @param format Format pattern 
   */
  public JetDate( final String date, final String format )
  {
    Utils.checkNull( date, "date cannot be null");
    
    this.date = date;
    javaDate = formatDate( date, format ); 
    
  }

  
  /**
   * Retrieve the exact string retrieved from the jet api response that 
   * represents a date.
   * @return date string 
   */
  @Override
  public String getDateString()
  {
    return date;
  }
  
  
  /**
   * Retrieve the date.
   * Note: this can be incorrect if the formatter failed.
   * @return  Date
   */
  @Override
  public Date getDate()
  {
    return javaDate;
  }
  
  
  /**
   * Attempt to format the date using the formatter.
   * @param date Date string
   * @param format Format pattern
   * @return Formatted Date or now 
   */
  private Date formatDate( final String date, final String format )
  {
    try {
      return ( new SimpleDateFormat( 
        format, Locale.ENGLISH )).parse( date );      
    } catch( ParseException e ) {
      APILog.error( LOG, e, "Failed to parse date", date, "with format", format );
      return new Date( 0 );
    }    
  }
}