/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheepguru.jetimport.api.jet.orders;

/**
 *
 * @author john
 */
public enum OrderExceptionState 
{
  /**
   * None specified 
   */
  NONE( "" ),
  
  /**
   * 
   */
  TOO_MANY_CANCELED( "exception - too many units cancelled" ),
  
  /**
   * 
   */
  MANUAL_CANCEL_TO_COMPLETE( "exception - jet manual canceled to complete state" ),
  
  /**
   * 
   */
  TOO_MANY_SHIPPED( "exception - too many units shipped" ),
  
  /**
   * 
   */
  REJECTED( "exception - order rejected" ),
  
  /**
   * 
   */
  RESOLVED( "resolved" );
  
  
  /**
   * Jet text
   */
  private final String text;
  
  
  private static final OrderExceptionState[] values = values();
  
  /**
   * Attempt to retrieve a OrderExceptionState by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static OrderExceptionState fromText( final String text )
    throws IllegalArgumentException
  {
    for ( final OrderExceptionState s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }  
  
  
  /**
   * Create a new OrderExceptionState enum instance
   * @param text Jet text
   */
  OrderExceptionState( final String text )
  {
    this.text = text;
  }
  
  
  /**
   * Get the jet api text
   * @return text
   */
  public String getText()
  {
    return text;
  }
}
