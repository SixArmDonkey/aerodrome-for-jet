
package com.sheepguru.jetimport.api.jet.product;

/**
 * Service level for the shipping exception item
 * @author John Quinn
 */
public enum ServiceLevel
{
  /**
   * Not Specified
   */
  NONE( "" ),

  /**
   * Ships 2 day
   */
  SECOND_DAY( "SecondDay" ),

  /**
   * Ships overnight
   */
  NEXT_DAY( "NextDay" ),

  /**
   * Scheduled?
   */
  SCHEDULED( "Scheduled" ),

  /**
   * Expedited shipping
   */
  EXPEDITED( "Expedited" ),

  /**
   * Standard shipping
   */
  STANDARD( "Standard" );

  /**
   * Jet API constant value
   */
  private final String val;

  
  /**
   * Attempt to create a ServiceLevel by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ServiceLevel fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ServiceLevel c : values())
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }      
  
  /**
   * Create a new ServiceLevel instance
   * @param val
   */
  ServiceLevel( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return jet api value
   */
  public String getText()
  {
    return val;
  }
}
