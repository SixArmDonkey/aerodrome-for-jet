/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet.products;

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

  private static ShipOverrideType[] values = values();
  
  /**
   * Attempt to create a ShipOverrideType by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ShipOverrideType fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ShipOverrideType c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }    
  

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
  public String getText()
  {
    return val;
  }
}
