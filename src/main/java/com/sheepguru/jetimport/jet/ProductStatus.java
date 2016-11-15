
package com.sheepguru.jetimport.jet;

/**
 * The product status retrieved from Jet
 *
 * @author John Quinn
 */
public enum ProductStatus
{
  /**
   * Status unknown
   */
  NONE( "" ),

  /**
   * he product is listed on Jet.com and can be purchased by a customer
   */
  AVAILABLE( "Available For Purchase" ),

  /**
   * The product can be listed on Jet.com and requires inventory
   */
  READY( "Ready To List" ),

  /**
   * More data is needed to list the product on Jet.com
   */
  DATA_NEEDED( "Listing Data Needed" ),

  /**
   * The SKU is current being processed in Jet's product catalog
   */
  PROCESSING( "Processing" );


  /**
   * Retrieve a ProductStatus by Jet API value
   * @param value value
   * @return status
   */
  public static ProductStatus fromValue( final String value )
  {
    for ( ProductStatus s : values())
    {
      if ( value.equals( s.getValue()))
        return s;
    }

    throw new IllegalArgumentException( value + " is not a valid value for ProductStatus" );
  }

  /**
   * The jet api value
   */
  private final String val;

  /**
   * Create a new ProductStatus instance
   * @param val jet api value
   */
  ProductStatus( final String val )
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
