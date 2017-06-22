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
import com.buffalokiwi.utils.Money;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * the item_price object for orders
 * @author John Quinn
 */
public class ItemPriceRec implements Jsonable
{
  /**
   * Item price 
   */
  private final Money price;  
  
  /**
   * The tax of the item
   */
  private final Money itemTax;
  
  /**
   * The shipping cost of the item
   */
  private final Money shippingPrice;
  
  /**
   * The shipping tax of the item
   */
  private final Money shippingTax;
  
  
  /**
   * Convert jet json into an instance 
   * @param json jet json 
   * @return this 
   */
  public static ItemPriceRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new ItemPriceRec(
      Utils.jsonNumberToMoney( json, "base_price" ),
      Utils.jsonNumberToMoney( json, "item_tax" ),
      Utils.jsonNumberToMoney( json, "item_shipping_cost" ),
      Utils.jsonNumberToMoney( json, "item_shipping_tax" )
    );
  }
  
  
  /**
   * Create an empty instance (zeros)
   */
  public ItemPriceRec()
  {
    this( new Money(), new Money(), new Money(), new Money());
  }
  
  
  /**  
   * Create a new instance.
   * Null is not allowed.
   * @param price Item price
   * @param itemTax Item tax
   * @param shippingPrice Shipping price 
   * @param shippingTax Shipping tax
   */
  public ItemPriceRec( 
    final Money price,
    final Money itemTax,
    final Money shippingPrice,
    final Money shippingTax
  ) {    
    Utils.checkNull( price, "price" );
    Utils.checkNull( itemTax, "itemTax" );
    Utils.checkNull( shippingPrice, "shippingPrice" );
    Utils.checkNull( shippingTax, "shippingTax" );
    
    this.price = new Money( price );
    this.itemTax = new Money( itemTax );
    this.shippingPrice = new Money( shippingPrice );
    this.shippingTax = new Money( shippingTax );
  }
  
  
  /**
   * Item price 
   * @return value
   */
  public Money getPrice()
  {
    return price;
  }
  
  /**
   * The tax of the item
   * @return value
   */
  public Money getItemTax()
  {
    return itemTax;
  }
  
  
  /**
   * The shipping cost of the item
   * @return value
   */
  public Money getShippingPrice()
  {
    return shippingPrice;
  }
  
  
  /**
   * The shipping tax of the item
   * @return value
   */
  public Money getShippingTax()
  {
    return shippingTax;
  }
  
  
  /**
   * Convert this object into jet json 
   * @return json 
   */
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "item_price", price.asBigDecimal())
      .add( "item_tax", price.asBigDecimal())
      .add( "item_shipping_cost", price.asBigDecimal())
      .add( "item_shipping_tax", price.asBigDecimal())
      .build();
  }
}
