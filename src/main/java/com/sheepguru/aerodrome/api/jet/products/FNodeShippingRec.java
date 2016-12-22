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
import com.sheepguru.aerodrome.api.jet.Utils;
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
public class FNodeShippingRec implements Jsonable
{
  /**
   * Shipping node id
   */
  private final String nodeId;

  /**
   * Ship exception data
   */
  private final List<ShippingExceptionRec> data;

  
  /**
   * Create an FNodeShipping instance from Jet JSON
   * @param json JSON 
   * @return object 
   */
  public static FNodeShippingRec fromJSON( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final JsonArray nodes = json.getJsonArray( "shipping_exceptions" );
    if ( nodes == null )
    {
      throw new IllegalArgumentException( 
        "json does not contain shipping_exceptions property (array)" );
    }
    
    final List<ShippingExceptionRec> ex = new ArrayList<>();
    
    for ( int i = 0; i < nodes.size(); i++ )
    {
      ex.add( ShippingExceptionRec.fromJSON( nodes.getJsonObject( i )));
    }
    
    return new FNodeShippingRec( json.getString( "fulfillment_node_id", "" ), ex );
  }
  

  /**
   * Create a new ShippingExcepionNode instance
   * @param nodeId Node id
   * @throws IllegalArgumentException
   */
  public FNodeShippingRec( final String nodeId )
  {
    Utils.checkNullEmpty( nodeId, "nodeId" );
    
    this.nodeId = nodeId;    
    this.data = Collections.unmodifiableList( new ArrayList<ShippingExceptionRec>());
  }
  
  
  /**
   * Create a new ShippingExcepionNode instance
   * @param nodeId Node id
   * @param exceptions Shipping Exceptions 
   * @throws IllegalArgumentException
   */
  public FNodeShippingRec( final String nodeId, final List<ShippingExceptionRec> exceptions )
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
  public void addItem( final ShippingExceptionRec data )
  {
    this.data.add( data );
  }


  /**
   * Retrieve the item data
   * @return data
   */
  public List<ShippingExceptionRec> getItemData()
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

    for ( ShippingExceptionRec item : data )
    {
      if ( item == null )
        continue;
      
      a.add( item.toJSON());
    }

    return a.build();
  }
}
