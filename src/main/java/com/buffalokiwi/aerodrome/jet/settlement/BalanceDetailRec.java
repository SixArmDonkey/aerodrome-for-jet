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

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Base class for OrderDetailRec and ReturnDetailRec.
 * This contains some shared properties.
 * 
 * @author John Quinn
 */
abstract class BalanceDetailRec implements Jsonable
{
  /**
   * BalanceDetailRec builder 
   */
  protected static class Builder
  {
    /**
     * The merchant price of the product being refunded to the customer
     */
    private Money merchantPrice = new Money();

    /**
     * The variable commission that was paid to the merchant or charged by Jet
     */
    private Money variableCommission = new Money();

    /**
     * The refunded fixed commission for a sale
     */
    private Money fixedCommission = new Money();

    /**
     * The returned tax charged to the customer for buying the item
     */
    private Money tax = new Money();

    /**
     * The tax charged on shipped revenue to be returned to the customer
     */
    private Money shippingTax = new Money();

    
    /**
     * The merchant price of the product being refunded to the customer
     * @param merchantPrice the merchantPrice to set
     * @return this
     */
    public Builder setMerchantPrice( final Money merchantPrice )
    {
      Utils.checkNull( merchantPrice, "merchantPrice" );
      this.merchantPrice = merchantPrice;
      return this;
    }

    
    /**
     * The variable commission that was paid to the merchant or charged by Jet
     * @param variableCommission the variableCommission to set
     * @return this
     */
    public Builder setVariableCommission( final Money variableCommission )
    {
      Utils.checkNull( variableCommission, "variableCommission" );
      this.variableCommission = variableCommission;      
      return this;
    }

    
    /**
     * The refunded fixed commission for a sale
     * @param fixedCommission the fixedCommission to set
     * @return this
     */
    public Builder setFixedCommission( final Money fixedCommission )
    {
      Utils.checkNull( fixedCommission, "fixedCommission" );
      this.fixedCommission = fixedCommission;
      return this;
    }

    
    /**
     * The returned tax charged to the customer for buying the item
     * @param tax the tax to set
     * @return this
     */
    public Builder setTax( final Money tax )
    {
      Utils.checkNull( tax, "tax" );
      this.tax = tax;
      return this;
    }

    
    /**
     * The tax charged on shipped revenue to be returned to the customer
     * @param shippingTax the shippingTax to set
     * @return this
     */
    public Builder setShippingTax( final Money shippingTax )
    {
      Utils.checkNull( shippingTax, "shippingTax" );
      this.shippingTax = shippingTax;
      return this;
    }    
  }
  
  
  /**
   * The merchant price of the product being refunded to the customer
   */
  private final Money merchantPrice;
  
  /**
   * The variable commission that was paid to the merchant or charged by Jet
   */
  private final Money variableCommission;
  
  /**
   * The refunded fixed commission for a sale
   */
  private final Money fixedCommission;
  
  /**
   * The returned tax charged to the customer for buying the item
   */
  private final Money tax;
  
  /**
   * The tax charged on shipped revenue to be returned to the customer
   */
  private final Money shippingTax;
  
  
  /**
   * Create a new BalanceDetailRec
   * @param merchantPrice The merchant price of the product being refunded to 
   * the customer
   * @param variableCommission The variable commission that was paid to the 
   * merchant or charged by Jet
   * @param fixedCommission The refunded fixed commission for a sale
   * @param tax The returned tax charged to the customer for buying the item
   * @param shippingTax The tax charged on shipped revenue to be returned to 
   * the customer
   */
  protected BalanceDetailRec( final Builder builder )
  {
    Utils.checkNull( builder, "builder" );
    
    this.merchantPrice = builder.merchantPrice;
    this.variableCommission = builder.variableCommission;
    this.fixedCommission = builder.fixedCommission;
    this.tax = builder.tax;
    this.shippingTax = builder.shippingTax;
  }
  
  
  /**
   * Build jet json from this object, and return the builder instance.
   * @return jet json builder 
   */
  protected final JsonObjectBuilder getJsonBuilder()
  {
    return Json.createObjectBuilder()
      .add("merchant_price", getMerchantPrice().toString())
      .add("jet_variable_commission", getVariableCommission().toString())
      .add("fixed_commission", getFixedCommission().toString())
      .add("tax", getTax().toString())
      .add("shipping_tax", getShippingTax().toString());
  }
  
  

  /**
   * The merchant price of the product being refunded to the customer
   * @return the merchantPrice
   */
  public Money getMerchantPrice()
  {
    return merchantPrice;
  }

  
  /**
   * The variable commission that was paid to the merchant or charged by Jet
   * @return the variableCommission
   */
  public Money getVariableCommission()
  {
    return variableCommission;
  }
  

  /**
   * The refunded fixed commission for a sale
   * @return the fixedCommission
   */
  public Money getFixedCommission()
  {
    return fixedCommission;
  }

  
  /**
   * The returned tax charged to the customer for buying the item
   * @return the tax
   */
  public Money getTax()
  {
    return tax;
  }

  
  /**
   * The tax charged on shipped revenue to be returned to the customer
   * @return the shippingTax
   */
  public Money getShippingTax()
  {
    return shippingTax;
  }  
}
