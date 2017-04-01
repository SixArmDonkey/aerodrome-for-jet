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
import com.buffalokiwi.aerodrome.jet.JetDate;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class SkuSalesDataRec 
{
  public static class Builder
  {
    /**
     * The product sku for this data
     */
    private String sku = "";

    /**
     * Jet sales rank of a SKU based off its exponential moving average sales by 
     * all retailers on the marketplace, updated daily.
     */  
    private int level0 = 0;

    /**
     * The last time Jet sales rank was updated
     */  
    private IJetDate salesRankUpdate = null;

    /**
     * Units of this SKU sold on Jet by all retailers over the last 30 days, 
     * updated daily. If there are no units sold, this field will not be presented.
     */  
    private int last30 = 0;

    /**
     * The last time units sold was updated
     */  
    private IJetDate unitsSoldUpdate = null;

    /**
     * Best offer overall
     */
    private final List<OfferRec> bestOffers = new ArrayList<>();

    /**
     * Best marketplace offer
     */
    private final List<OfferRec> marketplaceOffers = new ArrayList<>();
    
    
    /**
     * Create a new builder
     * @param sku The merchant sku 
     */
    public Builder( final String sku )
    {
      Utils.checkNull( sku, "sku" );
      this.sku = sku;
    }
    
    
    /**
     * Jet sales rank of a SKU based off its exponential moving average sales 
     * by all retailers on the marketplace, updated daily.
     * @param rank
     * @return this
     */
    public Builder setSalesRank( final int rank )
    {
      if ( rank < 0 )
        throw new IllegalArgumentException( "rank cannot be less than zero" );
      
      this.level0 = rank;
      return this;
    }
    
    
    /**
     * Set the last update for sales rank
     * @param date
     * @return this
     */
    public Builder setLastSalesRankUpdate( final IJetDate date )
    {
      this.salesRankUpdate = date;
      return this;
    }
    
    
    /**
     * Set the number of units sold in the last 30 days
     * @param units
     * @return this 
     */
    public Builder setUnitsSoldLast30( final int units )
    {
      if ( units < 0 )
        throw new IllegalArgumentException( "units cannot be less than zero" );
      
      this.last30 = units;
      return this;
    }
    
    
    /**
     * Set the last update for units sold
     * @param date
     * @return this
     */
    public Builder setUnitsSoldLastUpdate( final IJetDate date )
    {
      this.unitsSoldUpdate = date;
      return this;
    }
    
    
    /**
     * Add a list of best offers
     * @param offers Offers to add.  Set to null to clear the internal list.
     * @return this
     */
    public Builder addBestOffers( final List<OfferRec> offers )
    {
      if ( offers == null )
        this.bestOffers.clear();
      else
        this.bestOffers.addAll( offers );
      
      return this;
    }
    
    
    /**
     * Clear the best offers list
     * @return this
     */
    public Builder clearBestOffers()
    {
      bestOffers.clear();
      return this;
    }
    
    
    /**
     * Add a single best offer
     * @param offer offer
     * @return this
     */
    public Builder addBestOffer( final OfferRec offer )
    {
      Utils.checkNull( offer, "offer" );
      this.bestOffers.add( offer );      
      return this;      
    }
    
    
    /**
     * Add a list of marketplace offers.
     * @param offers offer list.  Set to null to clear the internal list.
     * @return this
     */
    public Builder addMarketplaceOffers( final List<OfferRec> offers )
    {
      if ( offers == null )
        this.marketplaceOffers.clear();
      else
        this.marketplaceOffers.addAll( offers );
      
      return this;
    }
    
    
    /**
     * Clear the marketplace offers
     * @return this
     */
    public Builder clearMarketplaceOffers()
    {
      this.marketplaceOffers.clear();
      return this;
    }
    
    
    /**
     * Adds a single marketplace offer 
     * @param offer Offer to add
     * @return this
     */
    public Builder addMarketplaceOffer( final OfferRec offer )
    {
      Utils.checkNull( offer, "offer" );
      marketplaceOffers.add( offer );
      return this;
    }
    
    
    /**
     * Build the sales data record
     * @return new record
     */
    public SkuSalesDataRec build()
    {
      return new SkuSalesDataRec( this );
    }
  }
  
  
  /**
   * The product sku for this data
   */
  private final String sku;

  /**
   * Jet sales rank of a SKU based off its exponential moving average sales by 
   * all retailers on the marketplace, updated daily.
   */  
  private final int level0;  

  /**
   * The last time Jet sales rank was updated
   */  
  private final IJetDate salesRankUpdate;  

  /**
   * Units of this SKU sold on Jet by all retailers over the last 30 days, 
   * updated daily. If there are no units sold, this field will not be presented.
   */  
  private final int last30;

  /**
   * The last time units sold was updated
   */  
  private final IJetDate unitsSoldUpdate;
  
  /**
   * Best offer overall
   */
  private final List<OfferRec> bestOffers;
  
  /**
   * Best marketplace offer
   */
  private final List<OfferRec> marketplaceOffers;


  
  protected SkuSalesDataRec( final Builder b )
  {
    if ( b.sku.trim().isEmpty())
      throw new IllegalArgumentException( "sku cannot be empty" );
    
    this.sku = b.sku;
    this.level0 = b.level0;
    this.salesRankUpdate = b.salesRankUpdate;
    this.last30 = b.last30;
    this.unitsSoldUpdate = b.unitsSoldUpdate;
    this.bestOffers = Collections.unmodifiableList( b.bestOffers );
    this.marketplaceOffers = Collections.unmodifiableList( b.marketplaceOffers );
  }  
  
  
  /**
   * Turn jet json into an instance of this object
   * @param sku sku
   * @param json json 
   * @return object 
   */
  public static SkuSalesDataRec fromJSON( final String sku, 
    final JsonObject json ) 
  {
    Utils.checkNull( json, "json" );
    
    final Builder b = new Builder( sku );
    
    final JsonObject sr = json.getJsonObject( "sales_rank" );
    if ( sr != null )
    {
      b.setSalesRank( sr.getInt( "level0", 0 ))
       .setLastSalesRankUpdate( JetDate.fromJetValueOrNull( sr.getString( "last_update", "" )));
    }
    
    final JsonObject us = json.getJsonObject( "units_sold" );
    
    if ( us != null )
    {
      b.setUnitsSoldLast30( json.getInt( "last_30_days", 0 ))
       .setUnitsSoldLastUpdate( JetDate.fromJetValueOrNull( json.getString( "last_update", "" )));
    }
    
    final List<OfferRec> boList = new ArrayList<>();
    
    final JsonArray bo = json.getJsonArray( "my_best_offer" );
    if ( bo != null )
    {
      for ( int i = 0; i < bo.size(); i++ )
      {
        boList.add( OfferRec.fromJSON( bo.getJsonObject( i )));
      }
    }
    
    Collections.reverse( boList );
    b.addBestOffers( boList );
    
    
    final List<OfferRec> moList = new ArrayList<>();
    
    final JsonArray mo = json.getJsonArray( "best_marketplace_offer" );
    if ( mo != null )
    {
      for ( int i = 0; i < mo.size(); i++ )
      {
        moList.add( OfferRec.fromJSON( mo.getJsonObject( i )));
      }
    }
    
    Collections.reverse( moList );
    b.addMarketplaceOffers( moList );
    
    return b.build();
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
  public int getSalesRank()
  {
    return level0;
  }
  
  
  /**
   * Get the units sold data
   * @return data
   */
  public int getUnitsSold()
  {
    return last30;
  }
  
  
  public IJetDate getLastSalesRankUpdate()
  {
    return salesRankUpdate;
  }
  
  
  public IJetDate getLastUnitsSoldUpdate()
  {
    return unitsSoldUpdate;
  }
  
  
  public OfferRec getBestOffer()
  {
    for ( final OfferRec rec : bestOffers )
    {
      return rec;
    }
    
    return null;
  }
  
  
  public OfferRec getBestMarketplaceOffer()
  {
    for ( final OfferRec rec : marketplaceOffers )
    {
      return rec;
    }
    
    return null;
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
