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
package com.buffalokiwi.aerodrome.jet.settlement;


/**
 * Represents a settlement state for the settlement response.
 * 
 * @author John Quinn
 */
public enum SettlementState
{
  /**
   * Not specified - do not send to jet.
   */
  NONE( "" ),
  
  /**
   * The payment was rejected by your bank. Please check your deposit 
   * information. 
   */
  REJECTED( "Rejected" ),
  
  /**
   * The payment has been deposited to your bank. 
   */
  DEPOSITED( "Paid" ),
  
  /**
   * The payment has been submitted to your bank and is awaiting to be 
   * deposited.
   */
  SUBMITTED( "Submitted" );
  
  
  /**
   * Enum values 
   */
  private static final SettlementState[] values = values();
  
  
  /**
   * Attempt to create a SettlementState by text value 
   * @param text value 
   * @return code 
   * @throws IllegalArgumentException if text is not found 
   */
  public static SettlementState fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final SettlementState c : values )
    {
      if ( c != null && c.getText().equalsIgnoreCase( text ))
        return c;
    }
    
    throw new IllegalArgumentException( "Invalid value " + String.valueOf( text ));
  }    
  
  
  /**
   * Jet text
   */
  private final String text;
  
  
  /**
   * Constructor
   * @param text jet text
   */
  SettlementState( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Retrieve the jet text
   * @return jet text
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
