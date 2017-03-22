/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.products;

import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.api.APILog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Represents a Date from a Jet API Response.
 * Uses format: yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z' 
 * @todo Remove all of this shit and replace it with the java 8 java.time stuff.
 * @author John Quinn
 */
public class ProductDate extends JetDate implements IJetDate 
{  
  /**
   * Default date format 
   */
  private static final String DEFAULT_FORMAT 
    = "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z";  
 
  /**
   * Logger 
   */
  private static final Log LOG = LogFactory.getLog( ProductDate.class );
  
  
  /**
   * Attempt to take some value and turn it into a valid JetDate.
   * If it isn't valid, then this returns null.
   * 
   * @param value Jet value 
   * @return date or null
   */
  public static ProductDate fromJetValueOrNull( final String value )
  {
    if ( value == null || value.isEmpty())
      return null;
    
    try {
      return new ProductDate( new SimpleDateFormat( 
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
  public ProductDate()
  {
    this( new Date());
  }
  
  
  /**
   * Create a new JetDate
   * @param date Date string
   */
  public ProductDate( final String date )
  {
    super( date, DEFAULT_FORMAT );
  }
  
  
  /**
   * Create a new JetDate
   * @param date date to use 
   */
  public ProductDate( final Date date )
  {
    super( date, DEFAULT_FORMAT );  
  }
}