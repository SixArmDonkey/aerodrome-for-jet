/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.products;


public enum ProductImageSlot
{
  MAIN( 0, "main" ),
  SWATCH( 0, "swatch" ),
  ONE( 1, "1" ),
  TWO( 2, "2" ),
  THREE( 3, "3" ),
  FOUR( 4, "4" ),
  FIVE( 5, "5" ),
  SIX( 6, "6" ),
  SEVEN( 7, "7" ),
  EIGHT( 8, "8" );

  private final int slot;
  private final String slotText;

  public static ProductImageSlot fromInt( final int slot )
  {
    switch( slot )
    {
      case 1: return ONE;
      case 2: return TWO;
      case 3: return THREE;
      case 4: return FOUR;
      case 5: return FIVE;
      case 6: return SIX;
      case 7: return SEVEN;
      case 8: return EIGHT;
    }

    throw new IllegalArgumentException( String.valueOf( slot ) + " is an illegal slot number.  Valid values are: 1-8, inclusive." );
  }

  ProductImageSlot( final int slot, final String slotText )
  {
    this.slot = slot;
    this.slotText = slotText;
  }
  
  
  /**
   * Check to see if this is a 1-8 slot and not main or swatch
   * @return 
   */
  public boolean isNumeric()
  {
    return slot != 0;
  }
  
  public int getSlot()
  {
    return slot;
  }
  

  @Override
  public String toString()
  {
    return slotText;
  }
}