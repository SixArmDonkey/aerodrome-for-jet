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
public enum OrderStatus 
{
  /**
   * order was created by Jet.com but not released for fulfillment
   */
  CREATED( "created" ),
  
  /**
   * order ready to be acknowledged by merchant
   */
  READY( "ready" ),
  
  /**
   * order was acknowledged by merchant
   */
  ACK( "acknowledged" ),
  
  /**
   * one part of the order has been shipped or cancelled
   */
  IN_PROGRESS( "inprogress" ),
  
  /**
   * all parts of the order have shipped or cancelled
   */
  COMPLETE( "complete" );
  
  /**
   * The jet text
   */
  private final String text;
  
  private static final OrderStatus[] values = values();
  
  /**
   * Attempt to retrieve a OrderStatus by text value 
   * @param text value 
   * @return status
   * @throws IllegalArgumentException if text is not found 
   */
  public static OrderStatus fromString( final String text )
    throws IllegalArgumentException
  {
    for ( final OrderStatus s : values )
    {
      if ( s.getText().equalsIgnoreCase( text ))
        return s;
    }
    
    throw new IllegalArgumentException( "Invalid value " + text );  
  }
  
  
  /**
   * Create a new OrderStatus enum instance
   * @param text The text for jet api 
   */
  OrderStatus( final String text )
  {
    this.text = text;
  }


  /**
   * Retrieve the jet api text
   * @return value for jet 
   */
  public String getText()
  {
    return text;
  }
}
