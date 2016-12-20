
package com.sheepguru.jetimport.api.jet.orders;

/**
 * Order acknowledgement status 
 * @author John Quinn
 */
public enum AckStatus 
{
  /**
   * None specified 
   */
  NONE( "" ),
  
  /**
   * The error occurred at the item level
   */
  REJECTED_ITEM( "rejected - item level error" ),
  
  /**
   * The ship to location is invalid
   */
  REJECTED_LOCATION( "rejected - ship from location not available" ),
  
  /**
   * The address does not support the desired shipping method 
   */
  REJECTED_METHOD( "rejected - shipping method not supported" ),
  
  /**
   * The address is invalid 
   */
  REJECTED_ADDRESS( "rejected - unfulfillable address" ),
  
  /**
   * All items in the order will be shipped 
   */
  ACCEPTED( "accepted" );
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  /**
   * Values cache
   */
  private static final AckStatus[] values = values();

  
  /**
   * Attempt to retrieve a AckStatus by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static AckStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final AckStatus s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }
    
  
  /**
   * Create a new AckStatus instance 
   * @param text jet api text
   */
  AckStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text 
   * @return text 
   */
  public String getText()
  {
    return text;
  }
}
