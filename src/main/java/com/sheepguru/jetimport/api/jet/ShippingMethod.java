/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet;


/**
 * Represents a shipping method for jet.
 * 
 * @author John Quinn
 */
public enum ShippingMethod 
{
  NONE( "" ),
  STANDARD_FREIGHT( "Freight" ),  
  DHL_GLOBAL( "DHL Global Mail" ),
  FEDEX_2_DAY( "FedEx 2 Day" ),
  FEDEX_EXPRESS_SAVER( "FedEx Express Saver" ),
  FEDEX_FIRST_OVERNIGHT( "FedEx First Overnight" ),
  FEDEX_GROUND( "FedEx Ground" ),
  FEDEX_HOME( "FedEx Home Delivery" ),
  FEDEX_PRI_OVERNIGHT( "FedEx Priority Overnight" ),
  FEDEX_SMART( "FedEx Smart Post" ),
  FEDEX_STD_OVERNIGHT( "FedEx Standard Overnight" ),
  FEDEX_FREIGHT( "Freight" ),
  ONTRAC_GROUND( "Ontrac Ground" ),
  UPS_2_DAY_AM( "UPS 2nd Day Air AM" ),
  UPS_2_DAY( "UPS 2nd Day Air" ),
  UPS_3_DAY( "UPS 3 Day Select" ),
  UPS_GROUND( "UPS Ground" ),
  UPS_MAIL( "UPS Mail Innovations" ),
  UPS_NEXT_DAY_SAVER( "UPS Next Day Air Saver" ),
  UPS_NEXT_DAY( "UPS Next Day Air" ),
  UPS_SUREPOST( "UPS SurePost" ),
  UPS_FIRST_CLASS_MAIL( "USPS First Class Mail" ),
  UPS_MEDIA_MAIL( "USPS Media Mail" ),
  USPS_EXPRESS( "USPS Priority Mail Express" ),
  USPS_PRIORITY( "USPS Priority Mail" ),
  USPS_STANDARD( "USPS Standard Post" );

  /**
   * Method text for Jet 
   */
  private final String text;
  
  private static final ShippingMethod[] values = values();
  
  /**
   * Attempt to create a ShipExceptionType by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShippingMethod fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShippingMethod c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }
  
  
  /**
   * Create a new ShippingMethod instance 
   * @param text jet text
   */
  ShippingMethod( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text for the shipping method.
   * @return text
   */
  public String getText()
  {
    return text;
  }
}