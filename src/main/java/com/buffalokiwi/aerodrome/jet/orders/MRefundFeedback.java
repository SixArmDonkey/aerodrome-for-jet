/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */


package com.buffalokiwi.aerodrome.jet.orders;



/**
 * Merchant-Initiated Item-Level Return Feedback.
 * @author John Quinn
 */
public enum MRefundFeedback 
{
  NONE( "" ),
  ITEM_DAMAGED( "Item damaged" ),
  WRONG_ITEM( "Wrong item" ),
  MISSING_PARTS( "Item is missing parts/accessories" ),
  RETURN_OUTSIDE( "Returned outside window" ),
  RESTOCKING_FEE( "Restocking fee" ),
  NOT_IN_PACKAGING( "Not shipped in original packaging" ),
  REROUTING_FEE( "Rerouting fee" ),
  ITEMS_NOT_RECEIVED( "Items not received" ),
  MINUS_SHIPPING( "Refunded minus shipping cost" );  
  
  
  /**
   * Text Value 
   */
  private final String text;

  /**
   * Text values list 
   */
  private final static MRefundFeedback[] values = values();


  /**
   * Attempt to retrieve a MRefundFeedback by text value 
   * @param text value 
   * @return type 
   * @throws IllegalArgumentException if text is not found 
   */
  public static MRefundFeedback fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final MRefundFeedback c : values )
    {
      if ( c.getText().equalsIgnoreCase( text ))
        return c;
    }

    throw new IllegalArgumentException( "Invalid value " + text );
  }      


  /**
   * Constructor 
   */
  MRefundFeedback( final String text )
  {
    this.text = text;
  }


  /**
   * Retrieve the text value 
   */
  public String getText()
  {
    return text;
  }
  
  
  @Override
  public String toString()
  {
    return text;
  }
}
