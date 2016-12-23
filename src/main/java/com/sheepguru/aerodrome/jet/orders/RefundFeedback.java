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

package com.sheepguru.aerodrome.jet.orders;

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
  NONE( "" ),
  
  /**
   * Some other reason
   */
  OTHER( "other" ),
  
  /**
   * The item was damaged
   */
  ITEM_DAMAGED( "item damaged" ),
  
  /**
   * Item was not returned in its original packaging
   */
  NOT_IN_PACKAGING( "not shipped in original packaging" ),
  
  /**
   * Customer opened the item
   */
  OPENED( "customer opened item" );
  
  
  /**
   * Enum values 
   */
  private static final RefundFeedback[] values = values();
  
  
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
  
  
  /**
   * Constructor
   * @param text jet api text
   */
  RefundFeedback( final String text )
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
