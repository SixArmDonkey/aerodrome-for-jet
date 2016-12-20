
package com.sheepguru.jetimport.api.jet.orders;

/**
 * Order item acknowledgement status 
 * @author John Quinn
 */
public enum ItemAckStatus 
{
  /**
   * Not specified 
   */
  NONE( "" ),
  
  /**
   * Can be fulfilled
   */
  FULFILLABLE( "fulfillable" ),
  
  /**
   * Cannot be fulfilled 
   */
  NOT_FULFILLABLE( "nonfulfillable" );
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  /**
   * Values cache
   */
  private static final ItemAckStatus[] values = values();

  
  /**
   * Attempt to retrieve a ItemAckStatus by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static ItemAckStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ItemAckStatus s : values )
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
  ItemAckStatus( final String text )
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
