
package com.sheepguru.jetimport.jet;

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
  private float quantity = 0F;


  /**
   * The inventory
   * @param id id
   * @param qty Quantity
   */
  public FNodeInventory( String id, float qty )
  {
    nodeId = id;
    quantity = qty;
  }


  public String getNodeId()
  {
    return nodeId;
  }

  public float getQuantity()
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
