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
package com.buffalokiwi.aerodrome.jet.settlement;

import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import javax.json.JsonObject;

/**
 * Order details for a SettlementRec response.
 * 
 * @author John Quinn
 */
public class OrderDetailRec extends BalanceDetailRec
{
  /**
   * OrderDetailRec builder 
   */
  public static class Builder extends BalanceDetailRec.Builder 
  {
    /**
     * Any charges for the return that the merchant is required to pay
     */
    private Money merchantReturnCharge = new Money();

    /**
     * If the item is being returned to Jet fulfillment center, this is the 
     * processing fee charged by Jet
     */
    private Money returnFee = new Money();
    
    
    /**
     * Build an OrderDetailRec instance 
     * @return instance 
     */
    public OrderDetailRec build()
    {      
      return new OrderDetailRec( this );
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
     * Any charges for the return that the merchant is required to pay
     * @param merchantReturnCharge the merchantReturnCharge to set
     * @return this
     */
    public Builder setMerchantReturnCharge( final Money merchantReturnCharge )
    {
      Utils.checkNull( merchantReturnCharge, "merchantReturnCharge" );
      this.merchantReturnCharge = merchantReturnCharge;
      return this;
    }

    
    /**
     * If the item is being returned to Jet fulfillment center, this is the 
     * processing fee charged by Jet
     * @param returnFee the returnFee to set
     * @return this
     */
    public Builder setReturnFee( final Money returnFee )
    {
      Utils.checkNull( returnFee, "returnFee" );
      this.returnFee = returnFee;
      return this;
    }
  } //..builder 
  
  
  /**
   * Any charges for the return that the merchant is required to pay
   */
  private final Money merchantReturnCharge;
  
  /**
   * If the item is being returned to Jet fulfillment center, this is the 
   * processing fee charged by Jet
   */
  private final Money returnFee;
  
  
  /**
   * Create a new OrderDetailRec from jet json 
   * @param json jet json 
   * @return instance
   */
  public static OrderDetailRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new Builder()
      .setMerchantPrice( Utils.jsonNumberToMoney( json, "merchant_price"))
      .setVariableCommission( Utils.jsonNumberToMoney( json, "jet_variable_commission"))
      .setFixedCommission( Utils.jsonNumberToMoney( json, "fixed_commission"))
      .setTax( Utils.jsonNumberToMoney( json, "tax" ))
      .setShippingTax( Utils.jsonNumberToMoney( json, "shipping_tax" ))
      .setMerchantReturnCharge( Utils.jsonNumberToMoney( json, "merchant_return_charge" ))
      .setReturnFee( Utils.jsonNumberToMoney( json, "return_processing_fee" ))
      .build();
  }
  
  
  /**
   * Constructor
   * @param builder builder instance
   */
  protected OrderDetailRec( final Builder builder )
  {
    super( builder );
    
    this.merchantReturnCharge = builder.merchantReturnCharge;
    this.returnFee = builder.returnFee;
  }
  
  
  /**
   * Turn this object into json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return getJsonBuilder()
      .add( "merchant_return_charge", merchantReturnCharge.toString())
      .add( "return_processing_fee", returnFee.toString())
      .build();
  }
  
  
  /**
   * Any charges for the return that the merchant is required to pay
   * @return the merchantReturnCharge
   */
  public Money getMerchantReturnCharge()
  {
    return merchantReturnCharge;
  }

  
  /**
   * If the item is being returned to Jet fulfillment center, this is the 
   * processing fee charged by Jet
   * @return the returnFee
   */
  public Money getReturnFee()
  {
    return returnFee;
  }  
}
