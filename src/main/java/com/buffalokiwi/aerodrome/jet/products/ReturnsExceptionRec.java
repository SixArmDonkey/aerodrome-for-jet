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

import com.buffalokiwi.aerodrome.jet.ShippingMethod;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Represents a ReturnsException query from Jet.
 * @author John Quinn
 */
public class ReturnsExceptionRec 
{
  /**
   * The number of days after purchase a customer can return the item.
   * 
   * 1 to 30 - Must be consistent with returns policy
   */
  private int timeToReturn;
  
  /**
   * The ID of the returns node that was created on partner.jet.com under fulfillment settings.
   * 
   * Must be a valid return node ID set up by the merchant
   */
  private List<String> returnLocationIds;
  
  /**
   * A specific shipping method. Jet will choose its default of FedEx ground
   */
  private List<ShippingMethod> returnShippingMethods;
  
  
  /**
   * Create one of deeze instances from some jet json.
   * @param json Jet json
   * @return yer object instance of this.
   */
  public static ReturnsExceptionRec fromJSON( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final JsonArray a = json.getJsonArray( "return_shipping_methods" );
    final List<ShippingMethod> methods = new ArrayList<>();
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        methods.add( ShippingMethod.fromText( a.getString( i )));
      }
    }
    
    
    return new ReturnsExceptionRec(
      json.getInt( "time_to_return", 30 ),
      Utils.jsonArrayToStringList( json.getJsonArray( "return_location_ids" )),
      methods 
    );
  }
  
  
  public ReturnsExceptionRec( final List<String> returnLocationIds )
  {
    Utils.checkNull( returnLocationIds, "returnLocationIds" );
    this.timeToReturn = 0;
    this.returnShippingMethods = new ArrayList<>();
    this.returnLocationIds = returnLocationIds;    
  }
  
  
  /**
   * Create a new instance.
   * @param timeToReturn Time to return
   * @param returnLocationIds Ids 
   * @param returnShippingMethods Methods 
   */
  public ReturnsExceptionRec( 
    final int timeToReturn,
    final List<String> returnLocationIds, 
    final List<ShippingMethod> returnShippingMethods )
  {
    if ( timeToReturn < 1 || timeToReturn > 30 )
      throw new IllegalArgumentException( "timeToReturn must be between 1 and 30 inclusive" );
    
    Utils.checkNull( returnLocationIds, "returnLocationIds" );
    Utils.checkNull( returnShippingMethods, "returnShippingMethods" );
    
    this.timeToReturn = timeToReturn;
    this.returnLocationIds = Collections.unmodifiableList( 
      new ArrayList<>( returnLocationIds ));
    this.returnShippingMethods = Collections.unmodifiableList( 
      new ArrayList<>( returnShippingMethods ));
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 17 * hash + this.timeToReturn;
    hash = 17 * hash + Objects.hashCode( this.returnLocationIds );
    hash = 17 * hash + Objects.hashCode( this.returnShippingMethods );
    return hash;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    final ReturnsExceptionRec other = (ReturnsExceptionRec) obj;
    if ( this.timeToReturn != other.timeToReturn ) {
      return false;
    }
    
    if ( this.returnLocationIds.size() != other.returnLocationIds.size())
      return false;
    
    if ( this.returnShippingMethods.size() != other.returnShippingMethods.size())
      return false;
    
    for ( final String id : this.returnLocationIds )
    {
      if ( !other.returnLocationIds.contains( id ))
        return false;
    }
    
    for ( final ShippingMethod method : this.returnShippingMethods )
    {
      if ( !other.returnShippingMethods.contains( method ))
        return false;
    }
    
    return true;
  }
  
  
  /**
   * The number of days after purchase a customer can return the item.
   * @return time
   */
  public int getTimeToReturn()
  {
    return timeToReturn;
  }
  
  
  /**
   * The ID of the returns node that was created on partner.jet.com under fulfillment settings.
   * @return ids
   */
  public List<String> getReturnLocationIds()
  {
    return returnLocationIds;
  }
  
  
  /**
   * A specific shipping method. Jet will choose its default of FedEx ground
   * @return methods
   */
  public List<ShippingMethod> getShippingMethods()
  {
    return returnShippingMethods;
  }
}
