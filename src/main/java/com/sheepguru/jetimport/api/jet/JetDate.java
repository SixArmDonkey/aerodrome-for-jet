/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APILog;
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
public class JetDate 
{  
  /**
   * Default date format 
   */
  private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z";  
 
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
   * Create a new JetDate
   * @param date Date string
   */
  public JetDate( final String date )
  {
    this( date, DEFAULT_FORMAT );
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
  public String getDateString()
  {
    return date;
  }
  
  
  /**
   * Retrieve the date.
   * Note: this can be incorrect if the formatter failed.
   * @return  Date
   */
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
        DEFAULT_FORMAT, Locale.ENGLISH )).parse( date );      
    } catch( ParseException e ) {
      APILog.error( LOG, e, "Failed to parse date", date, "with format", format );
      return new Date();
    }    
  }
}