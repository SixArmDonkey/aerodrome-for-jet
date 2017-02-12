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

/**
 * Reason for a return 
 * @author John Quinn
 */
public enum ReturnReason 
{
  /**
   * Unspecified
   */
  NONE( "" ),
  
  /**
   * User doens't want it 
   */
  DONT_WANT( "No longer want this item" ),
  
  /**
   * Wrong item was received
   */
  WRONG_ITEM( "Received the wrong item" ),
  
  /**
   * Item description was inaccurate on website
   */
  INACCURATE( "Website description is inaccurate" ),
  
  /**
   * Item was defective and does not work
   */
  DEFECTIVE( "Product is defective / does not work" ),
  
  /**
   * Item was damaged, box is ok
   */
  DAMAGED_ITEM( "Item arrived damaged - box intact" ),
  
  /**
   * Box was damaged, item is ok
   */
  DAMAGED_BOX( "Item arrived damaged - box damaged" ),
  
  /**
   * Package never arrived
   */
  NEVER_ARRIVED( "Package never arrived" ),
  
  /**
   * Package arrived late
   */
  ARRIVED_LATE( "Package arrived late" ),
  
  /**
   * Wrong quantity was received 
   */
  WRONG_QUANTITY( "Wrong quantity received" ),
  
  /**
   * User found a better deal elsewhere 
   */
  FOUND_BETTER_DEAL( "Better price found elsewhere" ),
  
  /**
   * Item was received as an unwanted gift 
   */
  UNWANTED_GIFT( "Unwanted gift" ),
  
  /**
   * Item was purchased on accident
   */
  ACCIDENTAL_ORDER( "Accidental order" ),
  
  /**
   * Purchase was unauthorized
   */
  UNAUTHORIZED_PURCHASE( "Unauthorized purchase" ),
  
  /**
   * Item has missing parts or accessories
   */
  MISSING_PARTS( "Item is missing parts / accessories" ),
  
  /**
   * Received product was refurbished when a new in box item was purchased
   */
  REFURBISHED( "Item is refurbished" ),
  
  /**
   * Item was past some expiration date
   */
  EXPIRED( "Item is expired" ),
  
  /**
   * Package arrived after the posted estimated delivery date
   */
  ARRIVED_AFTER_EST_DELIVERY( "Package arrived after estimated delivery date" );
  
  
  /**
   * Enum values 
   */
  private static final ReturnReason[] values = values();
  
  
  /**
   * Attempt to create a ReturnReason by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static ReturnReason fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final ReturnReason c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text.replace( "_", ""  )))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );
  }    
  
  
  /**
   * jet api text
   */
  private final String text;
  
  /**
   * Create a new instance  
   * @param text jet text
   */
  ReturnReason( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the text for the jet api 
   * @return jet text
   */
  public String getText()
  {
    return text;
  }
}
