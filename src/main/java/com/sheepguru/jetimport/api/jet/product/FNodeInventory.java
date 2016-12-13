
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Fulfillment node inventory/quantity record
 * @author john
 */
public class FNodeInventory implements Jsonable
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
  public static FNodeInventory fromJSON( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    return new FNodeInventory( json.getString( "fulfillment_node_id", "" ), json.getInt( "quantity", 0 ));
  }
  

  /**
   * The inventory
   * @param id id
   * @param qty Quantity
   */
  public FNodeInventory( String id, int qty )
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
