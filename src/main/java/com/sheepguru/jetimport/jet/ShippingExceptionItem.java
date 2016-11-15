
package com.sheepguru.jetimport.jet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * The shipping exception item for the shipping exception node array
 *
 * @author John Quinn
 */
public class ShippingExceptionItem implements Jsonable
{
  /**
   * Generic descriptions of shipment delivery times
   */
  private ServiceLevel serviceLevel = ServiceLevel.NONE;

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
  private String shippingMethod = "";

  /**
   * The type of shipping override, "Additional charge" or "Override charge"
   * (Override Charge could be used to lower shipping)
   */
  private ShipOverrideType overrideType = ShipOverrideType.NONE;

  /**
   * The amount added to your default shipping charge when
   * "OverrideType"= "Additional charge" and the total amount charged
   * when "OverrideType" = "Override charge"
   *
   * Logic
   * This field is required if override_type is populated
   */
  private float shippingChargeAmount = 0F;

  /**
   * Indicates if you want the product to be shipped exclusively (only with)
   * or restrictively (not including) via the shipping level of shipping
   * method provided
   */
  private ShipExceptionType shippingExceptionType = ShipExceptionType.NONE;


  /**
   * Generic descriptions of shipment delivery times
   * @return the serviceLevel
   */
  public ServiceLevel getServiceLevel() {
    return serviceLevel;
  }

  /**
   * Generic descriptions of shipment delivery times
   * @param serviceLevel the serviceLevel to set
   */
  public void setServiceLevel(ServiceLevel serviceLevel) {
    this.serviceLevel = serviceLevel;
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
  public String getShippingMethod() {
    return shippingMethod;
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
   * @param shippingMethod the shippingMethod to set
   */
  public void setShippingMethod(String shippingMethod) {
    this.shippingMethod = shippingMethod;
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
   * The type of shipping override, "Additional charge" or "Override charge"
   * (Override Charge could be used to lower shipping)
   * @param overrideType the overrideType to set
   */
  public void setOverrideType(ShipOverrideType overrideType) {
    this.overrideType = overrideType;
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
  public float getShippingChargeAmount() {
    return shippingChargeAmount;
  }

  /**
   * The amount added to your default shipping charge when
   * "OverrideType"= "Additional charge" and the total amount charged
   * when "OverrideType" = "Override charge"
   *
   * Logic
   * This field is required if override_type is populated
   * @param shippingChargeAmount the shippingChargeAmount to set
   */
  public void setShippingChargeAmount(float shippingChargeAmount) {
    this.shippingChargeAmount = shippingChargeAmount;
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

  /**
   * Indicates if you want the product to be shipped exclusively (only with)
   * or restrictively (not including) via the shipping level of shipping
   * method provided
   * @param shippingExceptionType the shippingExceptionType to set
   */
  public void setShippingExceptionType(ShipExceptionType shippingExceptionType) {
    this.shippingExceptionType = shippingExceptionType;
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
      o.add( "service_level", serviceLevel.getValue());

    if ( !shippingMethod.isEmpty())
      o.add( "shipping_method", shippingMethod );

    if ( overrideType != ShipOverrideType.NONE )
    {
      o.add( "override_type", overrideType.getValue());
      o.add( "shipping_charge_amount", shippingChargeAmount );
    }


    if ( shippingExceptionType != ShipExceptionType.NONE )
      o.add( "shipping_exception_type", shippingExceptionType.getValue());

    return o.build();
  }
}