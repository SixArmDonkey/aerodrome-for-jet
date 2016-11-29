
package com.sheepguru.jetimport.api.jet.product;

/**
 * Shipping exception type for shipping exception item.
 * Indicates if you want the product to be shipped exclusively (only with) or
 * restrictively (not including) via the shipping level of shipping method
 * provided
 *
 * @author John Quinn
 */
public enum ShipExceptionType
{
  /**
   * No exceptions
   */
  NONE( "" ),

  /**
   * The product should only be shipped using the shipping method or
   * shipping level provided
   */
  EXCLUSIVE( "exclusive" ),

  /**
   * The product cannot be shipped using the shipping method or shipping
   * level provided
   */
  RESTRICTED( "restricted" );

  /**
   * Jet value
   */
  private final String val;


  /**
   * Create a new ShipExceptionType
   * @param val jet value
   */
  ShipExceptionType( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the Jet API value
   * @return value
   */
  public String getValue()
  {
    return val;
  }
}