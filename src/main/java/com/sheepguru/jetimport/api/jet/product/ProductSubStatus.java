
package com.sheepguru.jetimport.api.jet.product;

/**
 * The Jet API Product sub status
 *
 * @author John Quinn
 */
public enum ProductSubStatus
{
  /**
   * Unknown status
   */
  NONE( "" ),

  /**
   *  Additional preferred attributes are requested but not required
   * (the product can be listed without clearing this sub status)
   */
  DATA_NEEDED( "Attribute Data Needed" ),

  /**
   * A price is needed to list the product on Jet.com
   */
  PRICE_NEEDED( "Price Needed" ),

  /**
   * A main image is needed to list the product on Jet.com
   */
  IMAGE_NEEDED( "Image Needed" );


  /**
   * Retrieve a Product sub status from the jet api value
   * @param val value
   * @return sub status
   */
  public static ProductSubStatus fromValue( final String val )
  {
    for ( ProductSubStatus s : values())
    {
      if ( val.equals( s.getValue()))
        return s;
    }

    throw new IllegalArgumentException( val + " is not a valid value for ProductSubStatus" );
  }


  /**
   * Jet API Value
   */
  private final String val;

  /**
   * Create a new ProductSubStatus instance
   * @param val Jet api value
   */
  ProductSubStatus( final String val )
  {
    this.val = val;
  }


  /**
   * Retrieve the jet api value
   * @return value
   */
  public String getValue()
  {
    return val;
  }
}
