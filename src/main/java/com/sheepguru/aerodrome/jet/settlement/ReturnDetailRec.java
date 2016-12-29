/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.sheepguru.aerodrome.jet.settlement;

import com.sheepguru.aerodrome.jet.Utils;
import com.sheepguru.utils.Money;
import javax.json.JsonObject;


/**
 * Return balance details for a SettlementRec response.
 * 
 * @author John Quinn
 */
public class ReturnDetailRec extends BalanceDetailRec
{
  /**
   * ReturnDetailRec builder 
   */
  public static class Builder extends BalanceDetailRec.Builder
  {
    /**
     * The shipping revenue charged to the customer for buying the item
     */
    private Money shippingRevenue = new Money();

    /**
     * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
     * for shipping the item to the customer
     */
    private Money shippingCharge = new Money();

    /**
     * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
     * for Jet to fulfill the customer order
     */
    private Money fulfillmentFee = new Money();

    
    /**
     * The shipping revenue charged to the customer for buying the item
     * @param shippingRevenue the shippingRevenue to set
     * @return this
     */
    public Builder setShippingRevenue( final Money shippingRevenue )
    {
      Utils.checkNull( shippingRevenue, "shippingRevenue" );
      this.shippingRevenue = shippingRevenue;
      return this;
    }

    
    /**
     * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
     * for shipping the item to the customer
     * @param shippingCharge the shippingCharge to set
     * @return this
     */
    public Builder setShippingCharge( final Money shippingCharge )
    {
      Utils.checkNull( shippingCharge, "shippingCharge" );
      this.shippingCharge = shippingCharge;
      return this;
    }

    
    /**
     * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
     * for Jet to fulfill the customer order
     * @param fulfillmentFee the fulfillmentFee to set
     * @return this
     */
    public Builder setFulfillmentFee( final Money fulfillmentFee )
    {
      Utils.checkNull( fulfillmentFee, "fulfillmentFee" );
      this.fulfillmentFee = fulfillmentFee;
      return this;
    }
    
    
    /**
     * The merchant price of the product being refunded to the customer
     * @param merchantPrice the merchantPrice to set
     * @return this
     */
    @Override
    public Builder setMerchantPrice( final Money merchantPrice )
    {
      super.setMerchantPrice( merchantPrice );
      return this;
    }

    
    /**
     * The variable commission that was paid to the merchant or charged by Jet
     * @param variableCommission the variableCommission to set
     * @return this
     */
    @Override
    public Builder setVariableCommission( final Money variableCommission )
    {
      super.setVariableCommission( variableCommission );
      return this;
    }

    
    /**
     * The refunded fixed commission for a sale
     * @param fixedCommission the fixedCommission to set
     * @return this
     */
    @Override
    public Builder setFixedCommission( final Money fixedCommission )
    {
      super.setFixedCommission( fixedCommission );
      return this;
    }

    
    /**
     * The returned tax charged to the customer for buying the item
     * @param tax the tax to set
     * @return this
     */
    @Override
    public Builder setTax( final Money tax )
    {
      super.setTax( tax );
      return this;
    }

    
    /**
     * The tax charged on shipped revenue to be returned to the customer
     * @param shippingTax the shippingTax to set
     * @return this
     */
    @Override
    public Builder setShippingTax( final Money shippingTax )
    {
      super.setShippingTax( shippingTax );
      return this;
    }
    
    
    /**
     * Build a ReturnDetailRec instance
     * @return instance
     */
    public ReturnDetailRec build()
    {
      return new ReturnDetailRec( this );
    }
  } //..builder
  
  
  /**
   * The shipping revenue charged to the customer for buying the item
   */
  private final Money shippingRevenue;
  
  /**
   * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
   * for shipping the item to the customer
   */
  private final Money shippingCharge;
  
  /**
   * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
   * for Jet to fulfill the customer order
   */
  private final Money fulfillmentFee;
    
  
  /**
   * Create a new OrderDetailRec from jet json 
   * @param json jet json 
   * @return instance
   */
  public static ReturnDetailRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new Builder()
      .setMerchantPrice( new Money( json.getString( "merchant_price", "0" )))
      .setVariableCommission( new Money( json.getString( "jet_variable_commission", "0" )))
      .setFixedCommission( new Money( json.getString( "fixed_commission", "0" )))
      .setTax( new Money( json.getString( "tax", "0" )))
      .setShippingTax( new Money( json.getString( "shipping_tax", "0" )))
      .setShippingRevenue( new Money( json.getString( "shipping_revenue", "0" )))
      .setShippingCharge( new Money( json.getString( "shipping_charge", "0" )))
      .setFulfillmentFee( new Money( json.getString( "fulfillment_fee", "0" )))
      .build();
  }
      
  
  /**
   * Constructor
   * @param b builder 
   */
  protected ReturnDetailRec( final Builder b )
  {
    super( b );
    this.shippingRevenue = b.shippingRevenue;
    this.shippingCharge = b.shippingCharge;
    this.fulfillmentFee = b.fulfillmentFee;
  }
  
  
  /**
   * Turn this object into json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return getJsonBuilder()
      .add( "shipping_revenue", shippingRevenue.toString())
      .add( "shipping_charge", shippingCharge.toString())
      .add( "fulfillment_fee", fulfillmentFee.toString())
      .build();
  }  
  
  
  /**
   * The shipping revenue charged to the customer for buying the item
   * @return the shippingRevenue
   */
  public Money getShippingRevenue()
  {
    return shippingRevenue;
  }

  
  /**
   * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
   * for shipping the item to the customer
   * @return the shippingCharge
   */
  public Money getShippingCharge()
  {
    return shippingCharge;
  }

  
  /**
   * If the item is sold via DBJ (Distributed by Jet), then this is the charge 
   * for Jet to fulfill the customer order
   * @return the fulfillmentFee
   */
  public Money getFulfillmentFee()
  {
    return fulfillmentFee;
  }  
}
