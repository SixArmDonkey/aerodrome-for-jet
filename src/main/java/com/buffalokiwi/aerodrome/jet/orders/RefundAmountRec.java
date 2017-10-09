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

package com.buffalokiwi.aerodrome.jet.orders;

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.aerodrome.jet.orders.ItemPriceRec;
import com.buffalokiwi.utils.Money;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Return refund amount 
 * 
 * @author John Quinn
 */
public class RefundAmountRec implements Jsonable
{
  public static class Builder
  {
    /**
     * Amount to be refunded for the given item in USD associated with the item 
     * itself. This should be the total cost for this item not the unit cost.
     */
    private Money principal = new Money();

    /**
     * Amount to be refunded for the given item in USD associated with tax that 
     * was charged for the item.
     */
    private Money tax = new Money();

    /**
     * Amount to be refunded for the given item in USD associated with the 
     * shipping cost that was allocated to this item.
     * 
     */
    private Money shippingCost = new Money();

    /**
     * Amount to be refunded for the given item in USD associated with the tax 
     * that was charged on shipping
     */
    private Money shippingTax = new Money();

    /**
     * @return the principal
     */
    public Money getPrincipal()
    {
      return principal;
    }

    /**
     * @param principal the principal to set
     */
    public Builder setPrincipal( Money principal )
    {
      Utils.checkNull( principal, "principal" );          
      this.principal = principal;
      return this;
    }

    /**
     * @return the tax
     */
    public Money getTax()
    {
      return tax;
    }

    /**
     * @param tax the tax to set
     */
    public Builder setTax( Money tax )
    {
      Utils.checkNull( tax, "tax" );
      this.tax = tax;
      return this;
    }

    /**
     * @return the shippingCost
     */
    public Money getShippingCost()
    {
      return shippingCost;
    }

    /**
     * @param shippingCost the shippingCost to set
     */
    public Builder setShippingCost( Money shippingCost )
    {
      Utils.checkNull( shippingCost, "shippingCost" );
      this.shippingCost = shippingCost;
      return this;
    }

    /**
     * @return the shippingTax
     */
    public Money getShippingTax()
    {
      return shippingTax;
    }

    /**
     * @param shippingTax the shippingTax to set
     */
    public Builder setShippingTax( Money shippingTax )
    {
      Utils.checkNull( shippingTax, "shippingTax" );
      this.shippingTax = shippingTax;
      return this;
    }
    
    
    public RefundAmountRec build()
    {
      return new RefundAmountRec( this );
    }
  }
  
  
  /**
   * Amount to be refunded for the given item in USD associated with the item 
   * itself. This should be the total cost for this item not the unit cost.
   */
  private final Money principal;
  
  /**
   * Amount to be refunded for the given item in USD associated with tax that 
   * was charged for the item.
   */
  private final Money tax;
  
  /**
   * Amount to be refunded for the given item in USD associated with the 
   * shipping cost that was allocated to this item.
   * 
   */
  private final Money shippingCost;
  
  /**
   * Amount to be refunded for the given item in USD associated with the tax 
   * that was charged on shipping
   */
  private final Money shippingTax;
  
  
  /**
   * Turn jet json into an RefundAmountRec instance 
   * @param json json
   * @return this 
   */
  public static RefundAmountRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    return new RefundAmountRec(
      Utils.jsonNumberToMoney( json, "principal" ),
      Utils.jsonNumberToMoney( json, "tax" ),
      Utils.jsonNumberToMoney( json, "shipping_cost" ),
      Utils.jsonNumberToMoney( json, "shipping_tax" )
    );
  }
  
  
  /**
   * @param price
   * @return 
   */
  public static RefundAmountRec fromItemPriceRec( final ItemPriceRec price )
  {
    Utils.checkNull( price, "price" );
    
    return new RefundAmountRec(
      price.getPrice(),
      price.getItemTax(),
      price.getShippingPrice(),
      price.getShippingTax()
    );
  }
  
  
  /**
   * Create a new refundAmountRec instance.
   * 
   * @param principal Amount to be refunded for the given item in USD associated 
   * with the item itself. This should be the total cost for this item not 
   * the unit cost
   * @param tax Amount to be refunded for the given item in USD associated with 
   * tax that was charged for the item.
   * @param shippingCost Amount to be refunded for the given item in USD 
   * associated with the shipping cost that was allocated to this item.
   * @param shippingTax Amount to be refunded for the given item in USD 
   * associated with the tax that was charged on shipping
   */
  public RefundAmountRec(
    final Money principal,
    final Money tax,
    final Money shippingCost,
    final Money shippingTax
  ) {    
    Utils.checkNull( principal, "principal" );
    Utils.checkNull( tax, "tax" );
    Utils.checkNull( shippingCost, "shippingCost" );
    Utils.checkNull( shippingTax, "shippingTax" );
    
    this.principal = principal;
    this.tax = tax;
    this.shippingCost = shippingCost;
    this.shippingTax = shippingTax;
  }
  
  
  public RefundAmountRec()
  {
    this( new Money(), new Money(), new Money(), new Money());
  }
  
  
  protected RefundAmountRec( final Builder b )
  {
    this( b.principal, b.tax, b.shippingCost, b.shippingTax );
  }
  
  
  /**
   * Returns principal + tax + shipping cost + shipping tax
   * @return total
   */
  public Money getTotal()
  {
    return principal.plus( tax ).plus(  shippingCost ).plus(  shippingTax );
  }
  
  
  public Builder toBuilder()
  {
    return new Builder()
      .setPrincipal( principal ).setShippingCost( shippingCost )
      .setShippingTax( shippingTax ).setTax( tax );
  }
  
  
  /**
   * Amount to be refunded for the given item in USD associated 
   * with the item itself. This should be the total cost for this item not 
   * the unit cost
   * @return principal
   */
  public Money getPrincipal()
  {
    return principal;
  }
  
  
  /**
   * Amount to be refunded for the given item in USD associated with 
   * tax that was charged for the item.
   * @return tax
   */
  public Money getTax()
  {
    return tax;
  }
  
  
  /**
   * Amount to be refunded for the given item in USD 
   * associated with the shipping cost that was allocated to this item.
   * @return shipping cost
   */
  public Money getShippingCost()
  {
    return shippingCost;
  }
  
  
  /**
   * Amount to be refunded for the given item in USD 
   * associated with the tax that was charged on shipping
   * @return shipping tax
   */
  public Money getShippingTax()
  {
    return shippingTax;
  }
  
  
  /**
   * Turn this into jet json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "principal", principal.asBigDecimal())
      .add( "tax", tax.asBigDecimal())
      .add( "shipping_cost", shippingCost.asBigDecimal())
      .add( "shipping_tax", shippingTax.asBigDecimal())
      .build();
  }
}
