
package com.sheepguru.jetimport.api.jet.product;

/**
 * Shipping Exception override type
 * The type of shipping override, "Additional charge" or "Override charge"
 * (Override Charge could be used to lower shipping)
 *
 * @author John Quinn
 */
public enum ShipOverrideType
{
  /**
   * None specified
   */
  NONE( "" ),

  /**
   * A charge that overrides the default shipping charge
   */
  OVERRIDE( "Override charge" ),

  /**
   * A charge that is added to the default shipping charge
   */
  ADDITIONAL( "Additional charge" );


  /**
   * The value
   */
  private final String val;


  /**
   * Create a new ShipOverrideType instance
   * @param val value
   */
  ShipOverrideType( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return val 
   */
  public String getValue()
  {
    return val;
  }
}
