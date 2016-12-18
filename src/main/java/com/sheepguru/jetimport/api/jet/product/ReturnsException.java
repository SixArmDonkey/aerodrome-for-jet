
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Represents a ReturnsException query from Jet.
 * @author John Quinn
 */
public class ReturnsException 
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
  public static ReturnsException fromJSON( final JsonObject json )
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
    
    
    return new ReturnsException(
      json.getInt( "time_to_return", 30 ),
      Utils.jsonArrayToStringList( json.getJsonArray( "return_location_ids" )),
      methods 
    );
  }
  
  
  /**
   * Create a new instance.
   * @param timeToReturn Time to return
   * @param returnLocationIds Ids 
   * @param returnShippingMethods Methods 
   */
  public ReturnsException( 
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
