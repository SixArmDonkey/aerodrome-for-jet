
package com.sheepguru.jetimport.api.jet.product;


/**
 * CPSIA Cautionary Statements
 * @author John Quinn
 */
public enum CPSIA
{
  NO_WARNING( "no warning applicable" ),
  IS_SMALL_PARTS( "choking hazard small parts" ),
  IS_SMALL_BALL( "choking hazard is a small ball" ),
  IS_MARBLE( "choking hazard is a marble" ),
  CONTAINS_SMALL_BALL( "choking hazard contains a small ball" ),
  CONTAINS_MARBLE( "choking hazard contains a marble" ),
  BALLOON( "choking hazard balloon" );

  /**
   * The statement text
   */
  private String text = "";

  /**
   * Attempt to retrieve a CPSIA by text value 
   * @param text value 
   * @return tax code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static CPSIA fromString( final String text )
    throws IllegalArgumentException
  {
    for ( final CPSIA c : values())
    {
      if ( c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
  
  /**
   * Create a new CPSIA instance
   * @param s Jet constant value 
   */
  CPSIA( final String s )
  {
    text = s;
  }


  /**
   * Retrieve the cpsia cautionary text
   * @return text
   */
  public String getText()
  {
    return text;
  }
}

