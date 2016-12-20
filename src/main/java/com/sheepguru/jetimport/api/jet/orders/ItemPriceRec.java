
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import com.sheepguru.utils.Money;
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
      new Money( json.getString( "base_price", "0" )),
      new Money( json.getString( "item_tax", "0" )),
      new Money( json.getString( "item_shipping_cost", "0" )),
      new Money( json.getString( "item_shipping_tax", "0" ))
    );
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
      .add( "item_price", price.floatValue())
      .add( "item_tax", price.floatValue())
      .add( "item_shipping_cost", price.floatValue())
      .add( "item_shipping_tax", price.floatValue())
      .build();
  }
}
