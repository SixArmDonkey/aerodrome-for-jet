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


package com.buffalokiwi.aerodrome.jet.products;

import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.ISO8601Date;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
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
  private final IJetDate lastUpdate;
  
  /**
   * A date format
   */
  //private final DateFormat fmt = new SimpleDateFormat( 
  //  "yyyy-MM-dd'T'HH:mm:ss'.0000000'Z", Locale.ENGLISH );
  
  
  /**
   * Create a new OfferRec instance from Jet Json.
   * @param json Json 
   * @return object  
   */
  public static OfferRec fromJSON( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new OfferRec(
      json.getString( "shipping_method", "" ),
      Utils.jsonNumberToMoney( json.getJsonNumber( "item_price")),
      Utils.jsonNumberToMoney( json.getJsonNumber( "shipping_price" )),
      ISO8601Date.fromJetValueOrNull( json.getString( "last_update", "" ))
    );
  }
  
  
  /**
   * Create a new OfferRec instance 
   * @param shippingMethod null
   * @param itemPrice item price 
   * @param shippingPrice ship price
   * @param lastUpdate last update 
   */
  public OfferRec(
    final String shippingMethod,
    final Money itemPrice,
    final Money shippingPrice,
    final IJetDate lastUpdate
  ) 
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
   * 
   * This may return null.
   * 
   * @return date
   */
  public IJetDate getLastUpdate()
  {
    return lastUpdate;
  }
}