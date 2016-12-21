
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.JetDate;
import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.ShippingCarrier;
import com.sheepguru.jetimport.api.jet.ShippingMethod;
import com.sheepguru.jetimport.api.jet.ShippingServiceLevel;
import com.sheepguru.jetimport.api.jet.Utils;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author john
 */
public class OrderDetailRec implements Jsonable
{
  /**
   * The shipping carrier that is delivering the shipment
   */
  private final ShippingCarrier requestShippingCarrier;
  
  /**
   * The shipping method for the given shipment_id
   */
  private final ShippingMethod requestShippingMethod;
  
  /**
   * Service level the customer will receive
   */
  private final ShippingServiceLevel requestServiceLevel;
  
  /**
   * Expected date that the shipment will be tendered to the given carrier
   */
  private final JetDate requestShipBy;
  
  /**
   * Date the customer has been promised delivery
   */
  private final JetDate requestDeliveryBy;

  
  /**
   * Turn jet json into an instance of this record
   * @param json Jet json object from api response 
   * @return instance
   */
  public static OrderDetailRec fromJson( final JsonObject json )
  {
    if ( json == null )
      throw new IllegalArgumentException( "json cannot be null" );
    
    return new OrderDetailRec(
      ShippingCarrier.fromText( json.getString( "request_shipping_carrier", "" )),
      ShippingMethod.fromText( json.getString( "request_shipping_method", "" )),
      ShippingServiceLevel.fromText( json.getString( "request_service_level", "" )),
      new JetDate( json.getString( "request_ship_by", "" )),
      new JetDate( json.getString( "request_delivery_by", "" ))
    );
  }
  
  
  /**
   * Create a new OrderDetailRec instance 
   * @param requestShippingCarrier The shipping carrier that is delivering 
   * the shipment
   * @param requestShippingMethod The shipping method for the given shipment_id
   * @param requestServiceLevel Service level the customer will receive
   * @param requestShipBy Expected date that the shipment will be tendered to 
   * the given carrier
   * @param requestDeliveryBy  Date the customer has been promised delivery
   */
  public OrderDetailRec(
    final ShippingCarrier requestShippingCarrier,
    final ShippingMethod requestShippingMethod,
    final ShippingServiceLevel requestServiceLevel,
    final JetDate requestShipBy,
    final JetDate requestDeliveryBy
  ) {
    
    Utils.checkNull( requestShippingCarrier, "requestShippingCarrier" );
    Utils.checkNull( requestShippingMethod, "requestShippingMethod" );
    Utils.checkNull( requestServiceLevel, "requestServiceLevel" );
    Utils.checkNull( requestShipBy, "requestShipBy" );
    Utils.checkNull( requestDeliveryBy, "requestDeliveryBy" );
    
    this.requestShippingCarrier = requestShippingCarrier;
    this.requestShippingMethod = requestShippingMethod;
    this.requestServiceLevel = requestServiceLevel;
    this.requestShipBy = requestShipBy;
    this.requestDeliveryBy = requestDeliveryBy;
  }

  
  /**
   * Get The shipping carrier that is delivering 
   * the shipment
   * @return the requestShippingCarrier
   */
  public ShippingCarrier getRequestShippingCarrier() 
  {
    return requestShippingCarrier;
  }

  
  /**
   * Get The shipping method for the given shipment_id
   * @return the requestShippingMethod
   */
  public ShippingMethod getRequestShippingMethod() 
  {
    return requestShippingMethod;
  }

  
  /**
   * Get Service level the customer will receive
   * @return the requestServiceLevel
   */
  public ShippingServiceLevel getRequestServiceLevel() 
  {
    return requestServiceLevel;
  }

  
  /**
   * Get Expected date that the shipment will be tendered to 
   * the given carrier
   * @return the requestShipBy
   */
  public JetDate getRequestShipBy() 
  {
    return requestShipBy;
  }

  
  /**
   * Get Date the customer has been promised delivery
   * @return the requestDeliveryBy
   */
  public JetDate getRequestDeliveryBy() 
  {
    return requestDeliveryBy;
  }
  
  
  /**
   * Turn this object into jet json  
   * @return jet json 
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "request_shipping_carrier", requestShippingCarrier.getText())
      .add( "request_shipping_method", requestShippingMethod.getText())
      .add( "request_service_level", requestServiceLevel.getText())
      .add( "request_ship_by", requestShipBy.getDateString())
      .add( "request_delivery_by", requestDeliveryBy.getDateString())
      .build();
  }
}