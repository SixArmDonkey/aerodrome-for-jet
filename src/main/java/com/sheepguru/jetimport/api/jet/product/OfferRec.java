

package com.sheepguru.jetimport.api.jet.product;

import com.sheepguru.jetimport.api.jet.Utils;
import com.sheepguru.utils.Money;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.json.JsonObject;

/**
 * Jet has "offer" records within the SalesData report.  
 * This represents a best offer, or a best marketplace offer.
 * 
 * @author John Quinn
 */
public class OfferRec 
{
  /**
   * The shipping method specified for your best price. For the time being, 
   * this will always be null.
   */
  private final String shippingMethod;
  
  /**
   * The item price for one unit of the SKU based on your best retail price 
   * (item and shipping price)
   */
  private final Money itemPrice;
  
  /**
   * The shipping price for one unit of the SKU based on your best retail 
   * price (item and shipping price)
   */
  private final Money shippingPrice;
  
  /**
   * The last time these prices were updated. The last_update time does not 
   * necessarily change if the best price does not change, even if the price 
   * is more up to date than this time.
   */
  private final String lastUpdate;
  
  /**
   * A date format
   */
  //private final DateFormat fmt = new SimpleDateFormat( 
  //  "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z", Locale.ENGLISH );
  
  
  /**
   * Create a new OfferRec instance from Jet Json.
   * @param json Json 
   * @return object  
   * @throws ParseException if the date cant be turned into a date object 
   */
  public static OfferRec fromJSON( final JsonObject json )
    throws ParseException 
  {
    Utils.checkNull( json, "json" );
    
    return new OfferRec(
      json.getString( "shipping_method", "" ),
      new Money( json.getString( "item_price", "" )),
      new Money( json.getString( "shipping_price", "" )),
      json.getString( "last_update", "" )
    );
  }
  
  
  /**
   * Create a new OfferRec instance 
   * @param shippingMethod null
   * @param itemPrice item price 
   * @param shippingPrice ship price
   * @param lastUpdate last update 
   * @throws ParseException if the date fails to format 
   */
  public OfferRec(
    final String shippingMethod,
    final Money itemPrice,
    final Money shippingPrice,
    final String lastUpdate
  ) throws ParseException
  {
    if ( shippingMethod == null )
      this.shippingMethod = "";
    else
      this.shippingMethod = shippingMethod;
    
    Utils.checkNull( itemPrice, "itemPrice" );
    Utils.checkNull( shippingPrice, "shippingPrice" );
    Utils.checkNull( lastUpdate, "lastUpdate" );
    
    this.lastUpdate = lastUpdate;//fmt.parse( lastUpdate );
    
    this.itemPrice = itemPrice;
    this.shippingPrice = shippingPrice;    
  }
  
  
  /**
   * The shipping method specified for your best price. For the time being, 
   * this will always be an empty string. (Jet API will return null).
   * @return empty string
   */
  public String getShippingMethod()
  {
    return shippingMethod;
  }
  
  
  /**
   * The item price for one unit of the SKU based on your best retail price 
   * (item and shipping price)
   * @return price
   */
  public Money getItemPrice()
  {
    return itemPrice;
  }
  
  
  /**
   * The shipping price for one unit of the SKU based on your best retail price 
   * (item and shipping price)
   * @return price
   */
  public Money getShippingPrice()
  {
    return shippingPrice;
  }
  
  
  /**
   * The last time these prices were updated. The last_update time does not 
   * necessarily change if the best price does not change, even if the price 
   * is more up to date than this time.
   * @return date
   */
  public String getLastUpdate()
  {
    return lastUpdate;
  }
}