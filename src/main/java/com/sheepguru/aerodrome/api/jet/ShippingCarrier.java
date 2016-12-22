/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.sheepguru.aerodrome.api.jet;

/**
 * Valid Jet shipping carriers.
 * @author John Quinn
 */
public enum ShippingCarrier 
{  
  NONE( "" ),
  FEDEX( "FedEx" ),
  FEDEX_SMARTPOST( "FedEx SmartPost" ),
  FEDEX_FREIGHT( "FedEx Freight" ),
  UPS( "UPS" ),
  UPS_FREIGHT( "UPS Freight" ),
  UPS_MAIL( "UPS Mail Innovations" ),
  UPS_SUREPOST( "UPS SurePost" ),
  ONTRAC( "OnTrac" ),
  ONTRAC_DIRECT( "OnTrac Direct Post" ),
  DHL( "DHL" ),
  DHL_GLOBAL( "DHL Global Mail" ),
  USPS( "USPS" ),
  CEVA( "CEVA" ),
  LASER( "Laser Ship" ),
  SPEE( "Spee Dee" ),
  DUIE_PYLE( "A Duie Pyle" ),
  A1( "A1" ),
  ABF( "ABF" ),
  APEX( "APEX" ),
  AVERITT( "Averitt" ),
  DYNAMEX( "Dynamex" ),
  EASTERN_CONNECTION( "Eastern Connection" ),
  ENSENDA( "Ensenda" ),
  ESTES( "Estes" ),
  LAND_AIR_EXPRESS( "Land Air Express" ),
  LONE_STAR( "Lone Star" ),
  MEYER( "Meyer" ),
  NEW_PENN( "New Penn" ),
  PILOT( "Pilot" ),
  PRESTIGE( "Prestige" ),
  RBF( "RBF" ),
  REDDAWAY( "Reddaway" ),
  RL( "RL Carriers" ),
  ROAD_RUNNER( "Roadrunner" ),
  SOUTHEASTERN_FREIGHT( "Southeastern Freight" ),
  UDS( "UDS" ),
  UES( "UES" ),
  YRC( "YRC" ),
  OTHER( "Other" );
  
  
  
  /**
   * Values cache
   */
  private static final ShippingCarrier[] values = values();

  
  /**
   * Attempt to retrieve a ShippingCarrier by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShippingCarrier fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShippingCarrier s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }  
  
  /**
   * Jet api text
   */
  private final String text;
  
  
  /**
   * Create a new ShippingCarrier instance
   * @param text text 
   */
  ShippingCarrier( final String text )
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