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
package com.sheepguru.aerodrome.api.jet.returns;

/**
 *
 * @author john
 */
public enum ChargeFeedback
{
  /**
   * Unspecified (do not send to jet)
   */
  NONE( "" ),
  
  /**
   * Other reason 
   */
  OTHER( "other" ),
  
  /**
   * Outside merchant policy 
   */
  OUTSIDE_POLICY( "outsideMerchantPolicy" ),
  
  /**
   * Not merchant error
   */
  NOT_MERCHANT_ERROR( "notMerchantError" ),
  
  /**
   * Wrong item 
   */
  WRONG_ITEM( "wrongItem" ),
  
  /**
   * Fraud 
   */
  FRAUD( "fraud" ),
  
  /**
   * Returned outside window 
   */
  OUTSIDE_WINDOW( "returnedOutsideWindow" );
  
  /**
   * Enum values 
   */
  private static final ChargeFeedback[] values = values();
  
  
  /**
   * Attempt to create a ChargeFeedback by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ChargeFeedback fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ChargeFeedback c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }    
  
  /**
   * Jet api text
   */
  private final String text;
  
  
  /**
   * Constructor
   * @param text jet api text
   */
  ChargeFeedback( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text
   * @return text
   */
  public String getText()
  {
    return text;
  }  
}
