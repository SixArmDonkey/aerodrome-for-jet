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

import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.api.IAPIDate;
import static java.lang.Character.FORMAT;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * An object representing the results from a Product Inventory query.
 * 
 * The inventory returned from this endpoint represents the number in the feed, 
 * not the quantity that is currently sellable on Jet.com
 * 
 * @author John Quinn
 */
public class ProductInventoryRec implements Jsonable
{
  /**
   * An array of fulfillment nodes to set inventory
   */
  private final List<FNodeInventoryRec> nodes;
  
  /**
   * The last update
   */
  private final IJetDate lastUpdate;
  
  
  /**
   * Create a ProductInventoryRec instance from Jet JSON
   * @param json Jet Json
   * @return Object 
   * @throws ParseException if the date can't be formatted 
   */
  public static ProductInventoryRec fromJSON( final JsonObject json )
    throws ParseException
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    final List<FNodeInventoryRec> n = new ArrayList<>();
    
    final JsonArray a = json.getJsonArray( "fulfillment_nodes" );
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        n.add( FNodeInventoryRec.fromJSON( a.getJsonObject( i )));
      }
    }
    
    return new ProductInventoryRec( n, json.getString( "inventory_last_update", "" ));
  }
  
  
  /**
   * Create a new ProductInventoryRec instance 
   * @param nodes Fulfillment nodes
   * @param lastUpdate Last update
   * @throws ParseException If last update can't be parsed 
   */
  public ProductInventoryRec( final List<FNodeInventoryRec> nodes, final String lastUpdate )
    throws ParseException
  {
    if ( nodes == null )
      throw new IllegalArgumentException( "nodes cannot be null" );
   
    this.nodes = Collections.unmodifiableList( new ArrayList<>( nodes ));
    if ( lastUpdate == null || lastUpdate.isEmpty())
      this.lastUpdate = null;
    else
      this.lastUpdate = JetDate.fromJetValueOrNull( lastUpdate );
  }
  
  
  /**
   * Retrieve the JSON representation of this object
   * @return JSON 
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder();
      
    if ( !nodes.isEmpty())
    {
      final JsonArrayBuilder a = Json.createArrayBuilder();
      
      for ( final FNodeInventoryRec rec : nodes )
      {
        a.add( rec.toJSON());
      }
      
      b.add( "fulfillment_nodes", a );
    }
    
    return b.build();         
  }
  
  
  /**
   * Retrieve the fulfillment nodes
   * @return nodes
   */
  public List<FNodeInventoryRec> getNodes()
  {
    return nodes;
  }
  
  
  /**
   * Attempt to retrieve an FNodeInventoryRec with the specified nodeId.
   * If none is available, this returns null.
   * @param nodeId Fulfillment node id 
   * @return inventory or null 
   */
  public FNodeInventoryRec getInventoryByNode( final String nodeId )
  {
    for ( final FNodeInventoryRec rec : nodes )
    {
      if ( rec.getNodeId().equals( nodeId ))
        return rec;
    }
    
    return null;
  }
  
  
  
  /**
   * Retrive the last update
   * @return date
   */
  public IJetDate getLastUpdate()
  {
    return lastUpdate;
  }
  
}
