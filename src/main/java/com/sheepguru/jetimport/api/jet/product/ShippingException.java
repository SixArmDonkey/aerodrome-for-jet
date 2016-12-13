
package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import com.sheepguru.utils.Money;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * The shipping exception item for the shipping exception node array
 * 
 * @author John Quinn
 */
public class ShippingException implements Jsonable
{
  /**
   * Generic descriptions of shipment delivery times
   */
  private final ServiceLevel serviceLevel;

  /**
   * A specific shipping method e.g. UPS Ground, UPS Next Day Air,
   * FedEx Home, Freight
   *
   * Logic
   * This should be used if you want the change to apply to a specific
   * shipping_method. If shipping_carrier is populated, this field will be
   * ignored. Use this field if you are defining your shippingoptions by
   * 'shipping_method'
   */
  private final ShippingMethod shippingMethod;

  /**
   * The type of shipping override, "Additional charge" or "Override charge"
   * (Override Charge could be used to lower shipping)
   */
  private final ShipOverrideType overrideType;

  /**
   * The amount added to your default shipping charge when
   * "OverrideType"= "Additional charge" and the total amount charged
   * when "OverrideType" = "Override charge"
   *
   * Logic
   * This field is required if override_type is populated
   */
  private final Money shippingChargeAmount;

  /**
   * Indicates if you want the product to be shipped exclusively (only with)
   * or restrictively (not including) via the shipping level of shipping
   * method provided
   */
  private final ShipExceptionType shippingExceptionType;

  
  /**
   * Create a new ShippingException instance 
   * @param serviceLevel Service level 
   * @param shippingMethod Shipping Method 
   * @param overrideType Shipping Override Type 
   * @param shippingChargeAmount Shipping charge
   * @param shippingExceptionType Exception Type 
   */
  public ShippingException( 
    final ServiceLevel serviceLevel,
    final ShippingMethod shippingMethod,
    final ShipOverrideType overrideType,
    final Money shippingChargeAmount,
    final ShipExceptionType shippingExceptionType  
  ) {
    Utils.checkNull( serviceLevel, "serviceLevel" );
    Utils.checkNull( shippingMethod, "shippingMethod" );
    Utils.checkNull( overrideType, "overrideType" );
    Utils.checkNull( shippingChargeAmount, "shippingChargeAmount" );
    Utils.checkNull( shippingExceptionType, "shippingExceptionType" );
    
    if ( shippingChargeAmount.lessThanZero())
      throw new IllegalArgumentException( "shippingChargeAmount cannot be less than zero" );
    
    this.serviceLevel = serviceLevel;
    this.shippingMethod = shippingMethod;
    this.overrideType = overrideType;
    this.shippingChargeAmount = shippingChargeAmount;
    this.shippingExceptionType = shippingExceptionType;    
  }
  

  /**
   * Generic descriptions of shipment delivery times
   * @return the serviceLevel
   */
  public ServiceLevel getServiceLevel() {
    return serviceLevel;
  }

  
  /**
   * A specific shipping method e.g. UPS Ground, UPS Next Day Air,
   * FedEx Home, Freight
   *
   * Logic
   * This should be used if you want the change to apply to a specific
   * shipping_method. If shipping_carrier is populated, this field will be
   * ignored. Use this field if you are defining your shippingoptions by
   * 'shipping_method'
   * @return the shippingMethod
   */
  public ShippingMethod getShippingMethod() {
    return shippingMethod;
  }

  
  /**
   * The type of shipping override, "Additional charge" or "Override charge"
   * (Override Charge could be used to lower shipping)
   * @return the overrideType
   */
  public ShipOverrideType getOverrideType() {
    return overrideType;
  }

  
  /**
   * The amount added to your default shipping charge when
   * "OverrideType"= "Additional charge" and the total amount charged
   * when "OverrideType" = "Override charge"
   *
   * Logic
   * This field is required if override_type is populated
   * @return the shippingChargeAmount
   */
  public Money getShippingChargeAmount() {
    return shippingChargeAmount;
  }

  
  /**
   * Indicates if you want the product to be shipped exclusively (only with)
   * or restrictively (not including) via the shipping level of shipping
   * method provided
   * @return the shippingExceptionType
   */
  public ShipExceptionType getShippingExceptionType() {
    return shippingExceptionType;
  }

  
  public static ShippingException fromJSON( final JsonObject o )
  {
    Utils.checkNull( o, "o" );
    
    return new ShippingException(
      ServiceLevel.fromText( o.getString( "service_level", "" )),
      ShippingMethod.fromText( o.getString( "shipping_method", "" )),
      ShipOverrideType.fromText( o.getString( "override_type", "" )),
      new Money( o.getString( "shipping_charge_amount", "" )),
      ShipExceptionType.fromText( o.getString( "shipping_exception_type", "" ))
    );    
  }


  /**
   * Retrieve the JSON object for this
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    JsonObjectBuilder o = Json.createObjectBuilder();
    if ( serviceLevel != ServiceLevel.NONE )
      o.add( "service_level", serviceLevel.getText());

    if ( shippingMethod != ShippingMethod.NONE )
      o.add( "shipping_method", shippingMethod.getText());

    if ( overrideType != ShipOverrideType.NONE )
    {
      o.add( "override_type", overrideType.getText());
      //..Don't use the currency formatted string here.  Jet wants a float.
      o.add( "shipping_charge_amount", shippingChargeAmount.toString());
    }


    if ( shippingExceptionType != ShipExceptionType.NONE )
      o.add( "shipping_exception_type", shippingExceptionType.getText());

    return o.build();
  }
}