
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.utils.Money;
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
      .add( "item_fees", itemFees.floatValue())
      .add( "fee_adjustments", a.build())
      .add( "regulatory_fees", regFees.floatValue())
      .build();
  }
  
}
