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

package com.sheepguru.aerodrome.jet.products;

/**
 * The type of rule that indicates how Jet member savings are allowed to be
 * applied to an item’s base price (which is referred to as map_price in
 * the API documentation).
 *
 * @author John Quinn
 */
public enum MAPType
{
  /**
   * Default, do not send to jet.
   */
  NONE( "", "Not Specified" ),
  
  /**
   * Jet member savings never applied to product
   */
  NOT_APPLIED( "103", "Not Applied" ),

  /**
   *  Jet member savings on product only visible to logged in Jet members
   */
  LOGGED_IN( "102", "Logged In" ),

  /**
   * no restrictions on product based pricing
   */
  NO_RESTRICTIONS( "101", "No Restrictions" );

  /**
   * The type
   */
  private final String type;
  
  private final String caption;

  
  private static final MAPType[] values = values();
  

  public static MAPType fromJet( final String val )
  {
    for ( MAPType m : values )
    {
      if ( val.equals( m.getType()))
        return m;
    }

    throw new IllegalArgumentException( val + " is an invalid MAPType value" );
  }

  /**
   * Create a new MAPType instance
   * @param type Jet constant value
   */
  MAPType( final String type, final String caption )
  {
    this.type = type;
    this.caption = caption;
  }

  /**
   * Retrieve the MAP implementation type
   * @return type
   */
  public String getType()
  {
    return type;
  }
  
  
  public String getCaption()
  {   
    return caption;
  }
  
  
  @Override
  public String toString()
  {
    return caption;
  }
}