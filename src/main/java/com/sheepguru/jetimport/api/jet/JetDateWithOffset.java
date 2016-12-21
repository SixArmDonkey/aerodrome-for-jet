
package com.sheepguru.jetimport.api.jet;

import com.sheepguru.jetimport.api.APILog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
