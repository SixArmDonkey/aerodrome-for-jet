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
import com.buffalokiwi.aerodrome.jet.ShippingServiceLevel;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * The shipping exception item for the shipping exception node array
 * 
 * @author John Quinn
 */
public class ShippingExceptionRec implements Jsonable
{
  public static class Builder
  {
    /**
     * Generic descriptions of shipment delivery times
     */
    private ShippingServiceLevel serviceLevel = ShippingServiceLevel.NONE;

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
    private ShippingMethod shippingMethod = ShippingMethod.NONE;

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
    private Money shippingChargeAmount = new Money();

    /**
     * Indicates if you want the product to be shipped exclusively (only with)
     * or restrictively (not including) via the shipping level of shipping
     * method provided
     */
    private ShipExceptionType shippingExceptionType = ShipExceptionType.EXCLUSIVE;

    /**
     * Generic descriptions of shipment delivery times
     * @param serviceLevel the serviceLevel to set
     * @return this
     */
    public Builder setServiceLevel( ShippingServiceLevel serviceLevel )
    {
      Utils.checkNull( serviceLevel, "serviceLevel" );
      this.serviceLevel = serviceLevel;
      return this;
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
     * @return this
     */
    public Builder setShippingMethod( ShippingMethod shippingMethod )
    {
      Utils.checkNull( shippingMethod, "shippingMethod" );
      this.shippingMethod = shippingMethod;
      return this;
    }

    /**
     * The type of shipping override, "Additional charge" or "Override charge"
     * (Override Charge could be used to lower shipping)
     * @param overrideType the overrideType to set
     * @return this
     */
    public Builder setOverrideType( ShipOverrideType overrideType )
    {
      Utils.checkNull( overrideType, "overrideType" );
      this.overrideType = overrideType;
      
      return this;
    }

    /**
     * The amount added to your default shipping charge when
     * "OverrideType"= "Additional charge" and the total amount charged
     * when "OverrideType" = "Override charge"
     *
     * Logic
     * This field is required if override_type is populated
     * @param shippingChargeAmount the shippingChargeAmount to set
     * @return this
     */
    public Builder setShippingChargeAmount( Money shippingChargeAmount )
    {
      Utils.checkNull( shippingChargeAmount, "shippingChargeAmount" );
      this.shippingChargeAmount = shippingChargeAmount;
      
      return this;
    }

    /**
     * Indicates if you want the product to be shipped exclusively (only with)
     * or restrictively (not including) via the shipping level of shipping
     * method provided
     * @param shippingExceptionType the shippingExceptionType to set
     * @return this
     */
    public Builder setShippingExceptionType( ShipExceptionType shippingExceptionType )
    {
      Utils.checkNull( shippingExceptionType, "shippingExceptionType" );
      this.shippingExceptionType = shippingExceptionType;
      return this;
    }
    
    
    public ShippingExceptionRec build()
    {
      return new ShippingExceptionRec( this );
    }    
  }
  
  
  /**
   * Generic descriptions of shipment delivery times
   */
  private final ShippingServiceLevel serviceLevel;

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
  public ShippingExceptionRec( 
    final ShippingServiceLevel serviceLevel,
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
   * Turn this into a builder
   * @return builder
   */
  public Builder toBuilder()
  {
    final Builder b = new Builder();
    b.serviceLevel = this.serviceLevel;
    b.shippingMethod = this.shippingMethod;
    b.overrideType = this.overrideType;
    b.shippingChargeAmount = this.shippingChargeAmount;
    b.shippingExceptionType = this.shippingExceptionType;            
    return b;
  }
  
  
  /**
   * Builder constructor 
   * @param b 
   */
  protected ShippingExceptionRec( final Builder b )
  {
    this.serviceLevel = b.serviceLevel;
    this.shippingMethod = b.shippingMethod;
    this.overrideType = b.overrideType;
    this.shippingChargeAmount = b.shippingChargeAmount;
    this.shippingExceptionType = b.shippingExceptionType;        
  }


  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode( this.serviceLevel );
    hash = 53 * hash + Objects.hashCode( this.shippingMethod );
    hash = 53 * hash + Objects.hashCode( this.overrideType );
    hash = 53 * hash + Objects.hashCode( this.shippingChargeAmount );
    hash = 53 * hash + Objects.hashCode( this.shippingExceptionType );
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
    final ShippingExceptionRec other = (ShippingExceptionRec) obj;
    if ( this.serviceLevel != other.serviceLevel ) {
      return false;
    }
    if ( this.shippingMethod != other.shippingMethod ) {
      return false;
    }
    if ( this.overrideType != other.overrideType ) {
      return false;
    }
    if ( !Objects.equals( this.shippingChargeAmount, other.shippingChargeAmount ) ) {
      return false;
    }
    if ( this.shippingExceptionType != other.shippingExceptionType ) {
      return false;
    }
    return true;
  }
  
  
  public ShippingExceptionRec createCopy()
  {
    return new ShippingExceptionRec( serviceLevel, shippingMethod, overrideType, 
      shippingChargeAmount, shippingExceptionType );
  }
  
  
  /**
   * Generic descriptions of shipment delivery times
   * @return the serviceLevel
   */
  public ShippingServiceLevel getServiceLevel() {
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

  
  public static ShippingExceptionRec fromJSON( final JsonObject o )
  {
    Utils.checkNull( o, "o" );
    
    return new ShippingExceptionRec(
      ShippingServiceLevel.fromText( o.getString( "service_level", "" )),
      ShippingMethod.fromText( o.getString( "shipping_method", "" )),
      ShipOverrideType.fromText( o.getString( "override_type", "" )),
      Utils.jsonNumberToMoney( o, "shipping_charge_amount" ),
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
    if ( serviceLevel != ShippingServiceLevel.NONE )
      o.add( "service_level", serviceLevel.getText());

    if ( shippingMethod != ShippingMethod.NONE )
      o.add( "shipping_method", shippingMethod.getText());

    if ( overrideType != ShipOverrideType.NONE )
    {
      o.add( "override_type", overrideType.getText());
      //..Don't use the currency formatted string here.  Jet wants a float.
      o.add( "shipping_charge_amount", shippingChargeAmount.asBigDecimal());
    }


    o.add( "shipping_exception_type", shippingExceptionType.getText());

    return o.build();
  }
}