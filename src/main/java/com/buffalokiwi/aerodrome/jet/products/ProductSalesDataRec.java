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

import com.buffalokiwi.aerodrome.jet.Utils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * A record that represents sales data for a single sku.
 * 
 * Analyze how your individual product price (item and shipping price) compares 
 * to the lowest individual product prices from the marketplace. These prices 
 * are only provided for SKUs that have the status “Available for Sale”. If a 
 * best price does not change, then the last_update time also will not change. 
 * If your inventory is zero, then these prices will not continue to be updated 
 * and will be stale. Note: It may take up to 24 hours to reflect any price 
 * updates from you and the marketplace.
 * 
 * Product pricing is one factor that Jet uses to determine which retailer wins 
 * a basket order. Jet determines what orders retailers will win based on the 
 * the product prices of all products in the order, base commission on those 
 * items as well as commission adjustments set via the Rules Engine. Commission 
 * adjustments set via the Rules Engine can be very effective in optimizing 
 * your win rate and profitability at the order level without having to have 
 * the absolute lowest item and shipping prices.
 * 
 * 
 * @author John Quinn
 */
public class ProductSalesDataRec 
{
  /**
   * The product sku for this data
   */
  private final String sku;
  
  /**
   * Sales rank data 
   */
  private final Map<Integer,String> salesRank;
  
  
  /**
   * Units sold data
   */
  private final Map<Integer,String> unitsSold;
  
  /**
   * Best offer overall
   */
  private final List<OfferRec> bestOffers;
  
  /**
   * Best marketplace offer
   */
  private final List<OfferRec> marketplaceOffers;


  /**
   * Turn jet json into an instance of this object
   * @param sku sku
   * @param json json 
   * @return object 
   * @throws ParseException if a date isnt parsed correctly
   */
  public static ProductSalesDataRec fromJSON( final String sku, 
    final JsonObject json ) throws ParseException
  {
    Utils.checkNull( json, "json" );
    
    final JsonObject sr = json.getJsonObject( "sales_rank" );
    final HashMap<Integer,String> srMap = new HashMap<>();
    
    if ( sr != null )
      srMap.put( json.getInt( "level_0", -1 ), json.getString( "last_update", "" ));
    else
      srMap.put( -1, "" );
    
    
    final JsonObject us = json.getJsonObject( "units_sold" );
    final HashMap<Integer,String> usMap = new HashMap<>();
    if ( us != null )
      usMap.put( json.getInt( "last_30_days", 0 ), json.getString( "last_update", "" ));
    else
      usMap.put( 0, "" );      
    
    final List<OfferRec> boList = new ArrayList<>();
    
    final JsonArray bo = json.getJsonArray( "my_best_offer" );
    if ( bo != null )
    {
      for ( int i = 0; i < bo.size(); i++ )
      {
        boList.add( OfferRec.fromJSON( bo.getJsonObject( i )));
      }
    }

    final List<OfferRec> moList = new ArrayList<>();
    
    final JsonArray mo = json.getJsonArray( "best_marketplace_offer" );
    if ( mo != null )
    {
      for ( int i = 0; i < mo.size(); i++ )
      {
        moList.add( OfferRec.fromJSON( mo.getJsonObject( i )));
      }
    }

    
    return new ProductSalesDataRec(
      sku,
      srMap,
      usMap,
      boList,
      moList
    );    
  }
  
  
  /**
   * Create a new ProductSalesDataRec instance 
   * @param sku Sku 
   * @param salesRank Data
   * @param unitsSold Data
   * @param bestOffers Data
   * @param marketplaceOffers Data
   */
  public ProductSalesDataRec( 
    final String sku,
    final Map<Integer,String> salesRank,
    final Map<Integer,String> unitsSold,
    final List<OfferRec> bestOffers,
    final List<OfferRec> marketplaceOffers
  ) {
    Utils.checkNullEmpty( sku, "sku" );
    Utils.checkNull( salesRank, "salesRank" );
    Utils.checkNull( unitsSold, "unitsSold" );
    Utils.checkNull( bestOffers, "bestOffers" );
    Utils.checkNull( marketplaceOffers, "marketplaceOffers" );
    
    
    this.sku = sku;
    this.salesRank = Collections.unmodifiableMap( new HashMap<>( salesRank ));
    this.unitsSold = Collections.unmodifiableMap( new HashMap<>( unitsSold ));
    this.bestOffers = Collections.unmodifiableList( new ArrayList<>( bestOffers ));
    this.marketplaceOffers = Collections.unmodifiableList( new ArrayList<>( marketplaceOffers ));
  }
  
  
  /**
   * Get the product sku this object represents.
   * @return sku
   */
  public String getSku()
  {
    return sku;
  }
  
  
  /**
   * Get the sales rank data
   * @return data
   */
  public Map<Integer,String> getSalesRank()
  {
    return salesRank;
  }
  
  
  /**
   * Get the units sold data
   * @return data
   */
  public Map<Integer,String> getUnitsSold()
  {
    return unitsSold;
  }
  
  
  /**
   * Get the best offers data
   * @return data
   */  
  public List<OfferRec> getBestOffers()
  {
    return bestOffers;
  }
  
  
  /**
   * Get the best marketplace offers data
   * @return data
   */
  public List<OfferRec> getBestMarketplaceOffers()
  {
    return marketplaceOffers;
  }
}
