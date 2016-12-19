
package com.sheepguru.jetimport.api.jet;

/**
 * Service level for the shipping exception item
 * @author John Quinn
 */
public enum ShippingServiceLevel
{
  /**
   * Not Specified
   */
  NONE( "" ),

  /**
   * Ships 2 day
   */
  SECOND_DAY( "Second Day" ),

  /**
   * Ships overnight
   */
  NEXT_DAY( "Next Day" ),

  /**
   * Scheduled?
   */
  SCHEDULED( "Freight" ),

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

  private static final ShippingServiceLevel[] values = values();
  
  
  /**
   * Attempt to create a ServiceLevel by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShippingServiceLevel fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShippingServiceLevel c : values )
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
  ShippingServiceLevel( final String val )
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
