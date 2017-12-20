/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */


package com.buffalokiwi.aerodrome.jet.reports;

import com.buffalokiwi.aerodrome.jet.ProductStatus;
import com.buffalokiwi.aerodrome.jet.ProductSubStatus;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * For bulk product status reports response.
 * 
 * @author John Quinn
 */
public class ProductStatusRec 
{
  /**
   * The merchant sku
   */
  private final String sku;
  
  /**
   * The product status 
   */
  private final ProductStatus status;
  
  /**
   * A list of product sub statuses
   */
  private final Collection<ProductSubStatus> sub;
  
  
  /**
   * Create a new instance from Jet Json
   * @param sku The merchant sku, which is the key from the parent json object 
   * @param data Json
   */
  public ProductStatusRec( final String sku, final JsonObject data )
  {
    Utils.checkNull( sku, "sku" );
    Utils.checkNull( data, "data" );
    
    if ( !data.containsKey( "status" ))
      throw new IllegalArgumentException( "Missing status property in json object" );
    
    this.sku = sku;
    status = ProductStatus.fromValue( data.getString( "status" ));
    
    final JsonArray ss = data.getJsonArray( "sub_status" );
    final ArrayList<ProductSubStatus> subList = new ArrayList<>();
    
    if ( ss != null )
    {
      ss.forEach( v -> {
        subList.add( ProductSubStatus.fromValue( v.toString()));
      });
    }
    
    
    sub = Collections.unmodifiableList( subList );
    
    
    //json.get
  }
  
  
  /**
   * Create a new ProductStatusRec
   * @param sku Merchant sku 
   * @param status Product status 
   * @param sub List of product sub statuses 
   */
  public ProductStatusRec( final String sku, final ProductStatus status, final Collection<ProductSubStatus> sub )
  {
    Utils.checkNull( sku, "sku" );
    Utils.checkNull( status, "status" );
    Utils.checkNull( sub, "sub" );
    
    this.sku = sku;
    this.status = status;
    this.sub = Collections.unmodifiableList( new ArrayList<>( sub ));
  }
  
  
  /**
   * Get the merchant sku 
   * @return 
   */
  public String getSku()
  {
    return sku;
  }
  
  
  /**
   * Get the product status 
   * @return 
   */
  public ProductStatus getStatus()
  {
    return status;
  }
  
  
  /**
   * Get the product sub status list
   * @return 
   */
  public Collection<ProductSubStatus> getSubStatus()
  {
    return sub;
  }
  
  
  /**
   * If there are any sub statuses listed
   * @return has dems
   */
  public boolean hasSubStatus()
  {
    return !sub.isEmpty();
  }
  
  
  private JsonArray subToJson()
  {
    final JsonArrayBuilder b = Json.createArrayBuilder();
    sub.stream().forEach( v -> b.add( v.getValue()));
    return b.build();
  }
  
  
  /**
   * Convert this to jet json 
   * @return json
   */
  public JsonObject toJson()
  {
    return Json.createObjectBuilder()
      .add( sku, Json.createObjectBuilder()
        .add( "status", status.getValue())
        .add(  "sub_status", subToJson())
        .build())
      .build();            
  }
}
