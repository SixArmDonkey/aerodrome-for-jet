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

package com.buffalokiwi.aerodrome.jet.orders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Refund feedback on an item from the merchant 
 * 
 * @author John Quinn
 */
public enum RefundFeedback 
{
  /**
   * Unspecified (use other for other).
   */
  NONE( "", true ),
  
  MISSING_PARTS( "Item is missing parts/accessories", true ),
  WRONG_ITEM( "Wrong Item", true ),
  ITEM_DAMAGED( "Item damaged", true ),
  OUTSIDE_WINDOW( "Returned outside window", true ),
  RESTOCKING_FEE( "Restocking fee", true ),
  NOT_IN_PACKAGING( "Not shipped in original packaging", true ),
  REROUTING_FEE( "Rerouting fee", true ),
  
  OTHER( "other", false ),
  ITEM_DAMAGED2( "item damaged", false ),
  NOT_IN_PACKAGING2( "not shipped in original packaging", false ),
  OPENED( "customer opened item", false );
  
  
  /**
   * Enum values 
   */
  private static final RefundFeedback[] values = values();
  
  
  public static RefundFeedback[] postableValues()
  {
    final RefundFeedback[] v = new RefundFeedback[values.length];
    
    int total = 0;
    for ( int i = 0; i < values.length; i++ )
    {
      if ( values[i].isPostable())
        v[total++] = values[i];
    }
    
    return Arrays.<RefundFeedback>copyOfRange( v, 0, total );
    
  }
  
  
  /**
   * Attempt to create a RefundFeedback by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static RefundFeedback fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final RefundFeedback c : values )
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
  
  private final boolean isPostable;
  
  
  /**
   * Constructor
   * @param text jet api text
   */
  RefundFeedback( final String text, final boolean isPostable )
  {
    this.text = text;
    this.isPostable = isPostable;
  }
  
  
  /**
   * If this value can be sent to Jet in some POST request.
   * @return 
   */
  public boolean isPostable()
  {
    return isPostable;
  }
  
  
  /**
   * Retrieve the jet text
   * @return text
   */
  public String getText()
  {
    return text;
  }
  
  
  @Override
  public String toString()
  {
    return getText();
  }
}
