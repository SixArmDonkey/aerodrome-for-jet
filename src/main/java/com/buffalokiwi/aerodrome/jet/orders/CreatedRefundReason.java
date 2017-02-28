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
package com.buffalokiwi.aerodrome.jet.orders;


/**
 * For some unknown reason, Jet decided to have the "created" refund response
 * return a completely different set of reasons than what is used for actually
 * creating a refund. 
 * 
 * @author john
 */
public enum CreatedRefundReason
{
  NONE( "" ),
  WRONG_QUANTITY( "wrong quantity received" ),
  WRONG_ITEM( "received wrong item than what was ordered" ),
  ACCIDENTAL_ORDER( "accidental order" ),
  ITEM_DAMAGED( "item is damaged/broken" ),
  ITEM_DEFECTIVE( "item is defective/does not work properly" ),
  ITEM_AND_BOX_DAMAGED( "shipping box and item are both damaged" ),
  INACCURATE( "item was different than website description" ),
  ARRIVED_LATE( "package arrived later than promised delivery date" ),
  NEVER_ARRIVED( "package never arrived" ),
  UNWANTED_GIFT( "unwanted gift" ),
  UNAUTHORIZED( "unauthorized purchase" ),
  BAD_PRICE( "better price available" ),
  DONT_WANT( "no longer need/want" );
  
  

  
  /**
   * Enum values 
   */
  private static final CreatedRefundReason[] values = values();
  
  
  /**
   * Attempt to create a CreatedRefundReason by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static CreatedRefundReason fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final CreatedRefundReason c : values )
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
  CreatedRefundReason( final String text )
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
  
  
  /**
   * Get the text for the jet api 
   * @return jet text
   */
  @Override
  public String toString()
  {
    return text;
  }  
  
    
  
}
