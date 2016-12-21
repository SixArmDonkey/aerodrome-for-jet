
package com.sheepguru.jetimport.api.jet.returns;

/**
 * To check for returns you will the utilize the Get Returns method. Please 
 * replace {status} with one of the following values:
 *
 *   created
 *   acknowledge - no longer in use
 *   inprogress
 *   completed by merchant
 * This will return a list of returns in that status.
 * 
 * @author John Quinn
 */
public enum ReturnStatus 
{
  /**
   * Created
   */
  CREATED( "created" ),
  
  /**
   * In progress
   */
  IN_PROGRESS( "inprogress" ),
  
  /**
   * Completed
   */
  COMPLETED( "completed by merchant" );
  
  /**
   * Enum values 
   */
  private static final ReturnStatus[] values = values();
  
  
  /**
   * Attempt to create a ReturnStatus by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ReturnStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ReturnStatus c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
  
  
  /**
   * Jet api text
   */
  private final String text;
  
  
  /**
   * Create a new ReturnStatus enum 
   * @param text jet text
   */
  ReturnStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text 
   * @return jet text
   */
  public String getText()
  {
    return text;
  }
}
