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
 * CPSIA Cautionary Statements
 * @author John Quinn
 */
public enum CPSIA
{
  NO_WARNING( "no warning applicable" ),
  IS_SMALL_PARTS( "choking hazard small parts" ),
  IS_SMALL_BALL( "choking hazard is a small ball" ),
  IS_MARBLE( "choking hazard is a marble" ),
  CONTAINS_SMALL_BALL( "choking hazard contains a small ball" ),
  CONTAINS_MARBLE( "choking hazard contains a marble" ),
  BALLOON( "choking hazard balloon" );

  /**
   * The statement text
   */
  private String text = "";

  private static final CPSIA[] values = values();
  
  /**
   * Attempt to retrieve a CPSIA by text value 
   * @param text value 
   * @return tax code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static CPSIA fromString( final String text )
    throws IllegalArgumentException
  {
    for ( final CPSIA c : values )
    {
      if ( c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }  
  
  /**
   * Create a new CPSIA instance
   * @param s Jet constant value 
   */
  CPSIA( final String s )
  {
    text = s;
  }


  /**
   * Retrieve the cpsia cautionary text
   * @return text
   */
  public String getText()
  {
    return text;
  }
}

