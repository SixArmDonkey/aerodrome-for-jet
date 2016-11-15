
package com.sheepguru.jetimport.jet;

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
  public String getValue()
  {
    return val;
  }
}
