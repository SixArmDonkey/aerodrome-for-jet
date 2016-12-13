
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


/**
 * A shipping exception node for shipping exceptions
 * @author John Quinn
 */
public class FNodeShipping implements Jsonable
{
  /**
   * Shipping node id
   */
  private final String nodeId;

  /**
   * Ship exception data
   */
  private final List<ShippingException> data;

  
  /**
   * Create an FNodeShipping instance from Jet JSON
   * @param json JSON 
   * @return object 
   */
  public static FNodeShipping fromJSON( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final JsonArray nodes = json.getJsonArray( "shipping_exceptions" );
    if ( nodes == null )
    {
      throw new IllegalArgumentException( 
        "json does not contain shipping_exceptions property (array)" );
    }
    
    final List<ShippingException> ex = new ArrayList<>();
    
    for ( int i = 0; i < nodes.size(); i++ )
    {
      ex.add( ShippingException.fromJSON( nodes.getJsonObject( i )));
    }
    
    return new FNodeShipping( json.getString( "fulfillment_node_id", "" ), ex );
  }
  

  /**
   * Create a new ShippingExcepionNode instance
   * @param nodeId Node id
   * @throws IllegalArgumentException
   */
  public FNodeShipping( final String nodeId )
  {
    Utils.checkNullEmpty( nodeId, "nodeId" );
    
    this.nodeId = nodeId;    
    this.data = Collections.unmodifiableList( new ArrayList<ShippingException>());
  }
  
  
  /**
   * Create a new ShippingExcepionNode instance
   * @param nodeId Node id
   * @param exceptions Shipping Exceptions 
   * @throws IllegalArgumentException
   */
  public FNodeShipping( final String nodeId, final List<ShippingException> exceptions )
  {
    Utils.checkNullEmpty( nodeId, "nodeId" );
    
    this.nodeId = nodeId;    
    
    Utils.checkNull( exceptions, "exceptions" );
    this.data = Collections.unmodifiableList( new ArrayList<>( exceptions ));
  }


  /**
   * Adds an item
   * @param data data to add
   */
  public void addItem( final ShippingException data )
  {
    this.data.add( data );
  }


  /**
   * Retrieve the item data
   * @return data
   */
  public List<ShippingException> getItemData()
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

    for ( ShippingException item : data )
    {
      if ( item == null )
        continue;
      
      a.add( item.toJSON());
    }

    return a.build();
  }
}
