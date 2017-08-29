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

package com.buffalokiwi.aerodrome.jet.products;

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * Fulfillment node price
 * @author John Quinn
 */
public class FNodePriceRec implements Jsonable
{
  /**
   * The fulfillment node assigned in the Jet Merchant Portal for a merchant
   * fulfillment node
   */
  private final String nodeId;

  /**
   * The price of the merchant SKU at the fulfillment node level
   */
  private final Money price;

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode( this.nodeId );
    hash = 11 * hash + Objects.hashCode( this.price );
    return hash;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    final FNodePriceRec other = (FNodePriceRec) obj;
    if ( !Objects.equals( this.nodeId, other.nodeId ) ) {
      return false;
    }
    
    return Objects.equals( this.price, other.price );
  }
  
  
  /**
   * Build this from Jet JSON
   * @param node node 
   * @return object
   */
  public static FNodePriceRec fromJSON( final JsonObject node )
  {
    if ( node == null )
      throw new IllegalArgumentException( "node cannot be null" );
    
    return new FNodePriceRec(
      node.getString( "fulfillment_node_id", "0" ),
      Utils.jsonNumberToMoney( node, "fulfillment_node_price" ) 
    );
  }

  
  /**
   * Create a deep copy of this object 
   * @return a copy 
   */
  public FNodePriceRec createCopy()
  {
    return new FNodePriceRec( nodeId, price );
  }
  

  /**
   * Create a new Fulfillment node 
   * @param id Node id 
   * @param price Price 
   * @throws IllegalArgumentException if id is null or empty or if price 
   * is less than zero.
   */
  public FNodePriceRec( final String id, final Money price )
    throws IllegalArgumentException 
  {
    if ( id == null || id.isEmpty())
      throw new IllegalArgumentException( "Fulfillment node id cannot be null or empty" );
    else if ( price == null || price.lessThanZero())
      throw new IllegalArgumentException( "Fulfillment node price cannot be less than zero" );
    this.nodeId = id;
    this.price = price;
  }


  /**
   * Retrieve the node id
   * @return id
   */
  public String getNodeId()
  {
    return nodeId;
  }

  
  /**
   * Retrieve the price
   * @return price
   */
  public Money getPrice()
  {
    return price;
  }

  
  /**
   * Turn this into json
   * @return
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "fulfillment_node_id", nodeId )
      .add( "fulfillment_node_price", price.asBigDecimal())
      .build();
  }
  
  
  @Override
  public String toString()
  {
    return price.toString();
  }
}
