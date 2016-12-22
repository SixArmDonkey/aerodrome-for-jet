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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


/**
 * This is stupid, but the order acknowledgement response is just different 
 * enough to warrant this.
 * 
 * @author John Quinn
 */
public class AckRequestRec implements Jsonable
{
  /**
   * Status to let Jet know whether you accept or reject the order.
   */
  private final AckStatus ackStatus;
  
  /**
   * Option merchant supplied order ID. Jet will map this ID to Jet's order_id 
   * and you can then use this ID in subsequent messages related to the order.
   */
  private final String altOrderId;
  
  /**
   * List of order items
   */
  private final List<AckRequestItemRec> items;
  
  
  /**
   * Turn jet json into an instance of this
   * @param json jet json
   * @return this
   */
  public static AckRequestRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new AckRequestRec(
      AckStatus.fromText( json.getString( "acknowledgement_status", "" )),
      json.getString( "alt_order_id", "" ),
      jsonToItemsList( json.getJsonArray( "order_items" ))       
    );    
  }
  
  
  /**
   * Turn jet json into a list of order items.
   * @param a json array
   * @return list
   */
  private static List<AckRequestItemRec> jsonToItemsList( final JsonArray a )
  {    
    final List<AckRequestItemRec> out = new ArrayList<>();
    
    if ( a == null )
      return out;
    
    //..This should contain what we need.
    for ( int i = 0; i < a.size(); i++ )
    {
      out.add( AckRequestItemRec.fromJson( a.getJsonObject( i )));
    }
    
    return out;
  }
  
  
  /**
   * Create an instance
   * @param ackStatus Status to let Jet know whether you accept or reject 
   * the order. 
   * 
   * Must be one of the following values:
   * rejected - other
   * rejected - fraud
   * rejected - item level error
   * rejected - ship from location not available
   * rejected - shipping method not supported
   * rejected - unfulfillable address
   * accepted
   * 
   * @param altOrderId Option merchant supplied order ID. Jet will map this 
   * ID to Jet's order_id and you can then use this ID in subsequent messages 
   * related to the order.
   * @param items This is an array of order items. Please see our order item 
   * array table below.
   */
  public AckRequestRec(
    final AckStatus ackStatus,
    final String altOrderId,
    final List<AckRequestItemRec> items 
  ) {
    Utils.checkNull( ackStatus, "ackStatus" );
    Utils.checkNull( altOrderId, "altOrderId" );
    Utils.checkNull( items, "items" );
    
    if ( ackStatus == AckStatus.NONE )
      throw new IllegalArgumentException( "ackStatus cannot be NONE" );
    
    this.ackStatus = ackStatus;
    this.altOrderId = altOrderId;
    this.items = Collections.unmodifiableList( items );
  }
  
  
  /**
   * Retrieve the status
   * @return status
   */
  public AckStatus getStatus()
  {
    return ackStatus;
  }
  
  
  /**
   * Retrieve the alternate order id
   * @return 
   */
  public String getAltOrderId()
  {
    return altOrderId;
  }
  
  
  /**
   * Get the list of order items
   * @return items
   */
  public List<AckRequestItemRec> getOrderItems()
  {
    return items;
  }
  
  
  /**
   * Turn this into jet json
   * @return jet json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "acknowledgement_status", ackStatus.getText())
      .add( "alt_order_id", altOrderId )
      .add( "order_items", getItemsJsonArray())
      .build();
  }
  
  
  /**
   * Turn the items into a json array for jet 
   * @return jet json 
   */
  private JsonArray getItemsJsonArray()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    
    for ( final AckRequestItemRec r : items )
    {
      ab.add( r.toJSON());
    }

    return ab.build();    
  }
}
