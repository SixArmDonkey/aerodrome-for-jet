
package com.sheepguru.jetimport.jet;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Fulfillment node price
 * @author John Quinn
 */
public class FNodePrice implements Jsonable
{
  /**
   * The fulfillment node assigned in the Jet Merchant Portal for a merchant
   * fulfillment node
   */
  private String nodeId = "";

  /**
   * The price of the merchant SKU at the fulfillment node level
   */
  private float price = 0F;


  public FNodePrice( String id, float price )
  {
    this.nodeId = id;
    this.price = Utils.round( price, 2 );
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
  public float getPrice()
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
      .add( "fulfillment_node_price", price )
      .build();
  }
}
