
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import javax.json.Json;
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
  private final String price;
  
  
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
      node.getString( "filfillment_node_price", "0" )    
    );
  }


  /**
   * Create a new Fulfillment node 
   * @param id Node id 
   * @param price Price 
   * @throws IllegalArgumentException if id is null or empty or if price 
   * is less than zero.
   */
  public FNodePriceRec( final String id, final String price )
    throws IllegalArgumentException 
  {
    if ( id == null || id.isEmpty())
      throw new IllegalArgumentException( "Fulfillment node id cannot be null or empty" );
    else if ( price == null || price.isEmpty())
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
  public String getPrice()
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
