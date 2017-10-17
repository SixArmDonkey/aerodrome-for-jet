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

import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


/**
 * Order totals data object.
 * @author John Quinn
 */
public class OrderTotalRec 
{
  /**
   * Price of the item
   */
  private final ItemPriceRec itemPrice;
  
  /**
   * The fees paid to Jet.com for the given quantity of the given merchant 
   * SKU in the given merchant order
   */
  private final Money itemFees;
  
  /**
   * The base and variable commissions paid to Jet on this order. Variable 
   * commissions are set through the Rules Engine in the Partner Portal
   */
  private final List<FeeAdjRec> adjustments;
  
  /**
   * Regulatory fees
   */
  private final Money regFees;
  
  
  /**
   * Turn jet json into an instance
   * @param json jet json 
   * @return object 
   */
  public static OrderTotalRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final List<FeeAdjRec> adj = new ArrayList<>();
    
    final JsonArray a = json.getJsonArray( "fee_adjustments" );
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        adj.add( FeeAdjRec.fromJson( a.getJsonObject( i )));
      }
    }
    
    return new OrderTotalRec(
      ItemPriceRec.fromJson( json.getJsonObject( "item_price" )),
      Utils.jsonNumberToMoney( json, "item_fees" ),
      adj,
      Utils.jsonNumberToMoney( json, "regulatory_fees" )
    );
  }
  
  
  /**
   * Create a new OrderTotalRec instance 
   * @param itemPrice Item price 
   * @param itemFees Fees paid to jet
   * @param adjustments Jet commissions 
   * @param regFees Fees
   */
  public OrderTotalRec(
    final ItemPriceRec itemPrice,
    final Money itemFees,
    final List<FeeAdjRec> adjustments,
    final Money regFees
  ) {
    this.itemPrice = itemPrice;
    this.itemFees = new Money( itemFees );
    this.adjustments = Collections.unmodifiableList( adjustments );
    this.regFees = new Money( regFees );
  }
  
  
  /**
   * Price of the item
   * @return item price 
   */
  public final ItemPriceRec getItemPrice()
  {
    return itemPrice;
  }
  
  
  /**
   * The fees paid to Jet.com for the given quantity of the given merchant 
   * SKU in the given merchant order
   * @return fees
   */
  public final Money getItemFees()
  {
    return itemFees;
  }
  
  
  /**
   * The base and variable commissions paid to Jet on this order. Variable 
   * commissions are set through the Rules Engine in the Partner Portal
   * @return adjustments 
   */
  public final List<FeeAdjRec> getAdjustments()
  {
    return adjustments;
  }
  
  
  /**
   * Regulatory fees
   * @return fees
   */
  public final Money getRegFees()
  {
    return regFees;
  }
  
  
  /**
   * Retrieve the sum of all fee adjustment records 
   * @return sum
   */
  public Money getFeeAdjustmentSum()
  {
    if ( adjustments.isEmpty())
      return new Money();
    
    return adjustments.stream().map( v -> v.getValue()).reduce((a,b) -> a.plus( b )).get();
  }
  
  
  /**
   * Retrieve the sum paid to Jet for this order.
   * @return total
   */
  public Money getFeeTotal()
  {
    return getItemFees().plus( getRegFees()).plus( getFeeAdjustmentSum());
  }
  
  
  /**
   * Retrieve the monetary value of this order.
   * This is the sum of all items + sum of shipping value - tax - shipping tax - 
   * sum of all fees paid to jet.
   * @return value 
   */
  public Money getOrderValue()
  {
    return itemPrice.getTotal().minus(  getFeeTotal());
  }

            
  /**
   * Turn this into a json object for jet api 
   * @return jet json 
   */
  public JsonObject toJSON()
  {
    
    final JsonArrayBuilder a = Json.createArrayBuilder();
    
    for ( final FeeAdjRec f : adjustments )
    {      
      a.add( f.toJSON());
    }

    
    return Json.createObjectBuilder()
      .add( "base_price", itemPrice.toJSON())
      .add( "item_fees", itemFees.asBigDecimal())
      .add( "fee_adjustments", a.build())
      .add( "regulatory_fees", regFees.asBigDecimal())
      .build();
  } 
}