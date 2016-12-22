/**
 * This file is part of the JetImport package, and is subject to the 
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

package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


/**
 * Represents a shipping request request body.
 * @author John Quinn
 */
public class ShipRequestRec implements Jsonable
{
  /**
   * Optional merchant supplied order ID. Jet will map this ID to 
   * Jet's order_id and you can then use this ID in subsequent messages 
   * relating to this order.
   */
  private final String altOrderId;
  
  /**
   * This is an array of shipments
   */
  private final List<ShipmentRec> shipments;
  
  
  /**
   * Turn jet json into an instance of this
   * @param json jet json 
   * @return this
   */
  public static ShipRequestRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new ShipRequestRec(
      json.getString( "alt_order_id", "" ),
      jsonToShipments( json.getJsonArray( "shipments" ))
    );
  }
  
  
  /**
   * Turn jet json into a list of shipments
   * @param a data
   * @return list 
   */
  private static List<ShipmentRec> jsonToShipments( final JsonArray a )
  {
    final List<ShipmentRec> out = new ArrayList<>();
    
    if ( a == null )
      return out;
    
    for ( int i = 0; i < a.size(); i++ )
    {
      out.add( ShipmentRec.fromJson( a.getJsonObject( i )));
    }
    
    return out;
  }
  
  
  /**
   * Create a new ShipRequestRec 
   * @param altOrderId alt order id 
   * @param shipments Shipments list 
   */
  public ShipRequestRec(
    final String altOrderId,
    final List<ShipmentRec> shipments
  ) {
    Utils.checkNull( altOrderId, "altOrderId" );
    Utils.checkNull( shipments, "shipments" );
    
    this.altOrderId = altOrderId;
    this.shipments = Collections.unmodifiableList( shipments );
  }
  
  
  /**
   * Get the custom order id..
   * @return id 
   */
  public String getAltOrderId()
  {
    return altOrderId;
  }
  
  
  /**
   * Get shipments list 
   * @return shipments 
   */
  public List<ShipmentRec> getShipments()
  {
    return shipments;
  }
  
  
  /**
   * Turn this into a json object for jet 
   * @return jet json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "alt_order_id", altOrderId )
      .add( "shipments", shipmentsToJsonArray())
      .build();
  }
  
  
  /**
   * Turn shipments into a json array 
   * @return json array 
   */
  private JsonArray shipmentsToJsonArray()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    for ( final ShipmentRec s : shipments )
    {
      if ( s == null )
        continue;
      
      ab.add( s.toJSON());
    }
    
    return ab.build();
  }
}
