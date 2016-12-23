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
package com.sheepguru.aerodrome.jet.orders;


/**
 * Jet's return message for whether the refund was accepted
 * @author John Quinn
 */
public enum RefundAcceptedStatus
{
  /**
   * Not specified - do not send to jet 
   */
  NONE( "" ),
  
  /**
   * invalid customer payment method 
   */
  REJECTED_PAYMENT( "rejected - customer card not valid" ),
  
  /**
   * Returns policy conflict
   */
  REJECTED_POLICY( "rejected - refund conflicts with returns policy" ),
  
  /**
   * Accepted and complete
   */
  ACCEPTED( "accepted" ),
  
  /**
   * Jet is processing
   */
  PROCESSING( "processing" ),
  
  /**
   * Refund is created and posted to jet 
   */
  CREATED( "created" );
  

  
  /**
   * Enum values 
   */
  private static final RefundAcceptedStatus[] values = values();
  
  
  /**
   * Attempt to create a RefundAcceptedStatus by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static RefundAcceptedStatus fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final RefundAcceptedStatus c : values )
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
   * Create dat ish
   * @param text jet text
   */
  RefundAcceptedStatus( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text
   * @return jet text
   */
  public String getText()
  {
    return text;
  }    
}
