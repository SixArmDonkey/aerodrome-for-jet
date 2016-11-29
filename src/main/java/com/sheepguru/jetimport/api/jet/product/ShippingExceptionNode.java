
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * A shipping exception node for shipping exceptions
 * @author John Quinn
 */
public class ShippingExceptionNode implements Jsonable
{
  /**
   * Shipping node id
   */
  private final String nodeId;

  /**
   * Ship exception data
   */
  private final List<ShippingExceptionItem> data = new ArrayList<>();


  /**
   * Create a new ShippingExcepionNode instance
   * @param nodeId Node id
   * @throws IllegalArgumentException
   */
  public ShippingExceptionNode( final String nodeId )
  {
    if ( nodeId == null || nodeId.isEmpty())
      throw new IllegalArgumentException( "nodeId cannot be empty" );

    this.nodeId = nodeId;
  }


  /**
   * Adds an item
   * @param data data to add
   */
  public void addItem( final ShippingExceptionItem data )
  {
    this.data.add( data );
  }


  /**
   * Retrieve the item data
   * @return data
   */
  public List<ShippingExceptionItem> getItemData()
  {
    return data;
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
   * Turn this object into JSON
   * @return JSON
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "fulfillment_node_id", nodeId )
      .add( "shipping_exceptions", itemsToJson())
      .build();
  }


  /**
   * Turn the items into a json array
   * @return json
   */
  private JsonArray itemsToJson()
  {
    JsonArrayBuilder a = Json.createArrayBuilder();

    for ( ShippingExceptionItem item : data )
    {
      if ( item == null )
        continue;
      
      a.add( item.toJSON());
    }

    return a.build();
  }
}
