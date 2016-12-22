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

package com.sheepguru.aerodrome.api.jet.products;

import com.sheepguru.aerodrome.api.jet.Jsonable;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Fulfillment node inventory/quantity record
 * @author john
 */
public class FNodeInventoryRec implements Jsonable
{
  /**
   * The fulfillment node assigned in the Jet Merchant Portal for a merchant
   * fulfillment node
   */
  private String nodeId = "";

  /**
   * The quantity of inventory for the given SKU in a given fulfillment node
   */
  private int quantity = 0;

  
  /**
   * Create a new FNodeInventory instance from Jet JSON 
   * @param json json object
   * @return object 
   */
  public static FNodeInventoryRec fromJSON( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    return new FNodeInventoryRec( json.getString( "fulfillment_node_id", "" ), json.getInt( "quantity", 0 ));
  }
  

  /**
   * The inventory
   * @param id id
   * @param qty Quantity
   */
  public FNodeInventoryRec( String id, int qty )
  {
    nodeId = id;
    quantity = qty;
  }


  public String getNodeId()
  {
    return nodeId;
  }

  public int getQuantity()
  {
    return quantity;
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
      .add( "quantity", quantity )
      .build();
  }

}
