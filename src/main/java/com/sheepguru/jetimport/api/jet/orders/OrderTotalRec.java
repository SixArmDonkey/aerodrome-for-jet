
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.Utils;
import com.sheepguru.utils.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
      new Money( json.getString( "item_fees", "0" )),
      adj,
      new Money( json.getString( "regulatory_fees", "0" ))      
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