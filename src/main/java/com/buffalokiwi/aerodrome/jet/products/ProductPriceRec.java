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
import com.buffalokiwi.aerodrome.jet.ISO8601Date;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * A record for a response from a product price retrieval.
 * 
 * @author John Quinn
 */
public class ProductPriceRec implements Jsonable
{
  /**
   * Fulfillment nodes 
   */
  private final List<FNodePriceRec> fNodes;
  
  /**
   * Price from jet api 
   */
  private final Money price;
  
  /**
   * Last update 
   */
  private final IJetDate lastUpdate;

  
  /**
   * Build this object from Jet JSON
   * @param json Jet JSON
   * @return object 
   * @throws ParseException 
   */
  public static ProductPriceRec fromJSON( final JsonObject json )
  {
    final JsonArray a = json.getJsonArray( "fulfillment_nodes" );
    final List<FNodePriceRec> nodes = new ArrayList<>();
    
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        nodes.add( FNodePriceRec.fromJSON( a.getJsonObject( i )));
      }
    }
    
    return new ProductPriceRec(
      Utils.jsonNumberToMoney( json.getJsonNumber( "price" )),
      ISO8601Date.fromJetValueOrNull( json.getString( "price_last_update", "" )),
      nodes
    );
  }
  
  
  
  /**
   * Create a new ProductPriceRec instance.
   * 
   * @param price The overall price that the merchant SKU is priced at. Note 
   * that this is the item price, and generally will not contain in it 
   * the shipping charge.
   * 
   * @param lastUpdate The timestamp for when this product's price was last 
   * updated
   * @param fNodes The price a retailer would like to set for this SKU sold at a fulfillment node
   * @throws ParseException if the date is invalid 
   */
  public ProductPriceRec( final Money price, final IJetDate lastUpdate, 
    final List<FNodePriceRec> fNodes ) 
  {
    if ( price == null || price.lessThanZero())
      throw new IllegalArgumentException( "price cannot be null or less than zero" );
    else if ( fNodes == null )
      throw new IllegalArgumentException( "fNodes cannot be null" );
    
    this.lastUpdate = lastUpdate;
    
    this.price = price;
    this.fNodes = Collections.unmodifiableList( new ArrayList<>( fNodes ));
  }
  
  
  /**
   * Retrieve the timestamp for when this product's price was last updated
   * This can be null
   * @return last update
   */
  public IJetDate getLastUpdate()
  {
    return lastUpdate;
  }
  
  
  /**
   * Retrieve the overall price that the merchant SKU is priced at. Note 
   * that this is the item price, and generally will not contain in it 
   * the shipping charge.
   * @return price 
   */
  public Money getPrice()
  {
    return price;    
  }
  
  
  /**
   * Retrieve the list of fulfillment nodes.
   * 
   * The price a retailer would like to set for this SKU sold at a fulfillment node
   * @return nodes list 
   */
  public List<FNodePriceRec> getFulfillmentNodes()
  {
    return fNodes;
  }
  
  
  /**
   * Retrieve the JSON representation of this object
   * @return JSON 
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "price", price.asBigDecimal());
    
    if ( !fNodes.isEmpty())
    {
      final JsonArrayBuilder a = Json.createArrayBuilder();
      
      for ( final FNodePriceRec rec : fNodes )
      {
        a.add( rec.toJSON());
      }
      
      b.add( "fulfillment_nodes", a );
    }
    
    return b.build();      
  }
}