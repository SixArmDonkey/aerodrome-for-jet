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

import com.buffalokiwi.api.APILog;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author john
 */
public class OrderItemRec 
{
  /**
   * Jet's unique identifier for an item in a merchant order
   */
  private final String orderItemId;
  
  /**
   * Optional merchant supplied ID for an item in an order
   */
  private final String altOrderItemId;
  
  /**
   * The merchant SKU that is in the given merchant order
   */
  private final String merchantSku;
  
  /**
   * Short product description
   */
  private final String title;
  
  /**
   * The quantity of the given merchant SKU that is in the given merchant order
   */
  private final int requestOrderQty;
  
  /**
   * The quanitity of the given merchant SKU that should be cancelled; 
   * present only if directCancelled event occured for the order
   */
  private final int requestOrderCancelQty;
  
  /**
   * This field is not used at this time
   */
  private final String adjReason;
  
  /**
   * The tax code associated with the given merchant SKU
   */
  private final String taxCode;
  
  /**
   * Image URL for the product
   */
  private final String url;
  
  /**
   * Price adjustment occured as a result of the partial cancel
   */
  private final Money priceAdj;
  
  /**
   * The fees paid to Jet.com for the given quantity of the given merchant 
   * SKU in the given merchant order
   */
  private final Money fees;
  
  /**
   * The base and variable commissions paid to Jet on this order. Variable 
   * commissions are set through the Rules Engine in the Partner Portal
   */
  private final List<FeeAdjRec> adjustments;
  
  /**
   * 
   */
  private final String taxInfo; //..No idea what this is. 
  
  /**
   * 
   */
  private final Money regFees;
  
  /**
   * Order item acknowledge status 
   */
  private final ItemAckStatus itemAckStatus;
  
  /**
   * Order item price 
   */
  private final ItemPriceRec itemPrice;
  
  private static final Log LOG = LogFactory.getLog( OrderItemRec.class );


  /**
   * A builder for creating instances of OrderItemRec 
   */
  public static class Builder
  {
    /**
     * Jet's unique identifier for an item in a merchant order
     */
    private String orderItemId = "";
    
    /**
     * Optional merchant supplied ID for an item in an order
     */
    private String altOrderItemId = "";
    
    /**
     * The merchant SKU that is in the given merchant order
     */
    private String merchantSku = "";
    
    /**
     * Short product description
     */
    private String title = "";
    
    /**
     * The quantity of the given merchant SKU that is in the given 
     * merchant order
     */
    private int requestOrderQty = 0;
    
    /**
     * The quanitity of the given merchant SKU that should be cancelled; 
     * present only if directCancelled event occured for the order
     */
    private int requestOrderCancelQty= 0;
    
    /**
     * This field is not used at this time
     */
    private String adjReason = "";
    
    /**
     * The tax code associated with the given merchant SKU
     */
    private String taxCode = "";
    
    /**
     * Image URL for the product
     */
    private String url = "";
    
    /**
     * Price adjustment occured as a result of the partial cancel
     */
    private Money priceAdj = new Money();
    
    /**
     * The fees paid to Jet.com for the given quantity of the given merchant 
     * SKU in the given merchant order
     */
    private Money fees = new Money();
    
    /**
     * The base and variable commissions paid to Jet on this order. Variable 
     * commissions are set through the Rules Engine in the Partner Portal
     */
    private final List<FeeAdjRec> adjustments = new ArrayList<>();
    
    /**
     * 
     */
    private String taxInfo = ""; //..No idea what this is. 
    
    /**
     * 
     */
    private Money regFees = new Money();
    
    /**
     * Order item acknowledge status 
     */
    private ItemAckStatus itemAckStatus = ItemAckStatus.NONE;
    
    private ItemPriceRec itemPrice = null;
  
    
    public Builder setItemPrice( final ItemPriceRec price )
    {
      Utils.checkNull( price, "price" );
      this.itemPrice = price;
      return this;
    }
    
    
    /**
     * Set the item acknowledged status  
     * @param status status 
     * @return this 
     */
    public Builder setItemAckStatus( final ItemAckStatus status )
    {
      Utils.checkNull( status, "status" );
      this.itemAckStatus = status;
      return this;
    }

    
    /**
     * Set Jet's unique identifier for an item in a merchant order
     * @param orderItemId the orderItemId to set
     * @return this
     */
    public Builder setOrderItemId( final String orderItemId )
    {
      Utils.checkNull( orderItemId, "orderItemId" );
      this.orderItemId = orderItemId;
      return this;
    }

    
    /**
     * Set Optional merchant supplied ID for an item in an order
     * @param altOrderItemId the altOrderItemId to set
     * @return this
     */
    public Builder setAltOrderItemId( final String altOrderItemId ) 
    {
      Utils.checkNull( altOrderItemId, "altOrderItemId" );
      this.altOrderItemId = altOrderItemId;
      return this;
    }

    
    /**
     * Set The merchant SKU that is in the given merchant order
     * @param merchantSku the merchantSku to set
     * @return this
     */
    public Builder setMerchantSku( final String merchantSku ) 
    {
      Utils.checkNull( merchantSku, "merchantSku" );
      this.merchantSku = merchantSku;
      return this;
    }

    
    /**
     * Set Short product description
     * @param title the title to set
     * @return this
     */
    public Builder setTitle( final String title ) 
    {
      Utils.checkNull( title, "title" );
      this.title = title;
      return this;
    }
    

    /**
     * Set The quantity of the given merchant SKU that is in the given merchant order
     * @param requestOrderQty the requestOrderQty to set
     * @return this
     */
    public Builder setRequestOrderQty( final int requestOrderQty ) 
    {
      if ( requestOrderQty < 0 )
      {
        throw new IllegalArgumentException( 
          "requestOrderQty cannot be less than zero" );
      }
      
      this.requestOrderQty = requestOrderQty;
      return this;
    }

    
    /**
     * Set The quanitity of the given merchant SKU that should be cancelled; 
     * present only if directCancelled event occured for the order
     * @param requestOrderCancelQty the requestOrderCancelQty to set
     * @return this
     */
    public Builder setRequestOrderCancelQty( final int requestOrderCancelQty ) 
    {
      if ( requestOrderCancelQty < 0 )
      {
        throw new IllegalArgumentException( 
          "requestOrderCancelQty cannot be less than zero" );
      }
      
      this.requestOrderCancelQty = requestOrderCancelQty;
      return this;
    }

    
    /**
     * Unused at this time. 
     * @param adjReason the adjReason to set
     * @return this
     */
    public Builder setAdjReason( final String adjReason ) 
    {
      Utils.checkNull( adjReason, "adjReason" );
      this.adjReason = adjReason;
      return this;
    }

    
    /**
     * Set The tax code associated with the given merchant SKU
     * @param taxCode the taxCode to set
     * @return this
     */
    public Builder setTaxCode( final String taxCode ) 
    {
      Utils.checkNull( taxCode, "taxCode" );
      this.taxCode = taxCode;
      return this;
    }

    
    /**
     * Set Image URL for the product
     * @param url the url to set
     * @return this
     */
    public Builder setUrl( final String url ) 
    {
      Utils.checkNull( url, "url" );
      this.url = url;
      return this;
    }

    
    /**
     * Set Price adjustment occured as a result of the partial cancel
     * @param priceAdj the priceAdj to set
     * @return this
     */
    public Builder setPriceAdj( final Money priceAdj ) 
    {
      Utils.checkNull( priceAdj, "priceAdj" );
      this.priceAdj = priceAdj;
      return this;
    }

    
    /**
     * Set The fees paid to Jet.com for the given quantity of the given 
     * merchant SKU in the given merchant order
     * @param fees the fees to set
     * @return this
     */
    public Builder setFees( final Money fees ) 
    {
      Utils.checkNull( fees, "fees" );
      this.fees = fees;
      return this;
    }

    
    /**
     * Set some adjustments 
     * The base and variable commissions paid to Jet on this order. 
     * Variable commissions are set through the Rules Engine in the 
     * Partner Portal
     * @param list list of em
     * @return this
     */
    public Builder setAdjustments( final List<FeeAdjRec> list )
    {
      this.adjustments.addAll( list );
      return this;
    }
    
    
    /**
     * Set tax info - undocumented.
     * @param taxInfo the taxInfo to set
     * @return this
     */
    public Builder setTaxInfo( final String taxInfo ) 
    {
      Utils.checkNull( taxInfo, "taxInfo" );
      this.taxInfo = taxInfo;
      return this;
    }

    
    /**
     * Set regulatory fees
     * @param regFees the regFees to set
     * @return this
     */
    public Builder setRegFees( final Money regFees ) 
    {
      Utils.checkNull( regFees, "regFees" );
      this.regFees = regFees;
      return this;
    }
    
    
    /**
     * Build the instance.
     * @return instance
     */
    public OrderItemRec build()
    {
      return new OrderItemRec( this );
    }    
  }
  
  
  /**
   * Turn some fee adjustment data from jet json into FeeAdjRec instances..
   * @param a data 
   * @return adjustments
   * @throws JetException 
   */
  private static List<FeeAdjRec> jsonToFeeAdj( final JsonArray a )
    throws JetException 
  {   
    final List<FeeAdjRec> adj = new ArrayList<>();
    
    if ( a == null )
      return adj;
        
    for ( int i = 0; i < a.size(); i++ )
    {
      try {
        adj.add( FeeAdjRec.fromJson( a.getJsonObject( i )));
      } catch( Exception e ) {
        APILog.error( LOG, e, "Failed to build FeeAdjRec instance at position", String.valueOf( i ));
        throw new JetException( "Failed to build FeeAdjRec", e );
      }
    }
    
    return adj;
  }
  
  
  /**
   * Create an OrderItemRec from jet json 
   * @param json jet json
   * @return instance
   */
  public static OrderItemRec fromJson( final JsonObject json )
    throws JetException
  {
    
    
    final Builder b = (new Builder())
      .setOrderItemId( json.getString( "order_item_id", "" ))
      .setAltOrderItemId( json.getString( "alt_order_item_id", "" ))
      .setMerchantSku( json.getString( "merchant_sku", "" ))
      .setTitle( json.getString( "product_title", "" ))
      .setRequestOrderQty( json.getInt( "request_order_quantity", 0 ))
      .setRequestOrderCancelQty( json.getInt( "request_order_cancel_qty", 0 ))
      .setAdjReason( json.getString( "adjustment_reason", "" ))
      .setTaxCode( json.getString( "item_tax_code", "" ))
      .setUrl( json.getString( "url", "" ))
      .setPriceAdj( Utils.jsonNumberToMoney( json.getJsonNumber( "price_adjustment" )))
      .setFees( Utils.jsonNumberToMoney( json.getJsonNumber( "item_fees" )))
      .setTaxInfo( json.getString( "tax_info", "" )) //..This might not work...
      .setRegFees( Utils.jsonNumberToMoney( json.getJsonNumber( "regulatory_fees" )))
      .setAdjustments( jsonToFeeAdj( json.getJsonArray( "fee_adjustments" )))
      .setItemAckStatus( ItemAckStatus.fromText( 
        json.getString( "order_item_acknowledgement_status", "" )));
    
    final JsonObject price = json.getJsonObject( "item_price" );
    if ( price != null )
      b.setItemPrice( ItemPriceRec.fromJson( price ));
    
    return b.build();
  }
  
  
  
  
  private OrderItemRec( final Builder b )
  {
    this.orderItemId = b.orderItemId;
    this.altOrderItemId = b.altOrderItemId;
    this.merchantSku = b.merchantSku;
    this.title = b.title;
    this.requestOrderQty = b.requestOrderQty;
    this.requestOrderCancelQty = b.requestOrderCancelQty;
    this.adjReason = b.adjReason;
    this.taxCode = b.taxCode;
    this.url = b.url;
    this.priceAdj = new Money( b.priceAdj );
    this.fees = new Money( b.fees );
    this.adjustments = Collections.unmodifiableList( b.adjustments );
    this.taxInfo = b.taxInfo; 
    this.regFees = new Money( b.regFees );
    this.itemAckStatus = b.itemAckStatus;
    
    if ( b.itemPrice == null )
      this.itemPrice = new ItemPriceRec(); 
    else
      this.itemPrice = b.itemPrice;    
  }


  public Builder toBuilder()
  {
    final Builder b = new Builder();
    b.orderItemId = this.orderItemId;
    b.altOrderItemId = this.altOrderItemId;
    b.merchantSku = this.merchantSku;
    b.title = this.title;
    b.requestOrderQty = this.requestOrderQty;
    b.requestOrderCancelQty = this.requestOrderCancelQty;
    b.adjReason = this.adjReason;
    b.taxCode = this.taxCode;
    b.url = this.url;
    b.priceAdj = new Money( this.priceAdj );
    b.fees = new Money( this.fees );
    b.adjustments.addAll( this.adjustments );
    b.taxInfo = this.taxInfo; 
    b.regFees = new Money( this.regFees );
    b.itemAckStatus = this.itemAckStatus;
    
    if ( this.itemPrice == null )
      b.itemPrice = new ItemPriceRec(); 
    else
      b.itemPrice = this.itemPrice;
    
    return b;
  }
  
  

  /**
   * Get Jet's unique identifier for an item in a merchant order
   * @return the orderItemId
   */
  public String getOrderItemId() 
  {  
    return orderItemId;
  }

  /**
   * Get Optional merchant supplied ID for an item in an order
   * @return the altOrderItemId
   */
  public String getAltOrderItemId() 
  {
    return altOrderItemId;
  }

  /**
   * Get The merchant SKU that is in the given merchant order
   * @return the merchantSku
   */
  public String getMerchantSku() 
  {
    return merchantSku;
  }

  /**
   * Get Short product description
   * @return the title
   */
  public String getTitle() 
  {
    return title;
  }

  /**
   * Get The quantity of the given merchant SKU that is in the given 
   * merchant order
   * @return the requestOrderQty
   */
  public int getRequestOrderQty() 
  {
    return requestOrderQty;
  }
  
  
  public ItemPriceRec getItemPrice()
  {
    return itemPrice;
  }
  

  /**
   * Get The quanitity of the given merchant SKU that should be cancelled; 
   * present only if directCancelled event occured for the order
   * @return the requestOrderCancelQty
   */
  public int getRequestOrderCancelQty() 
  {
    return requestOrderCancelQty;
  }

  /**
   * Get This field is not used at this time
   * @return the adjReason
   */
  public String getAdjReason() 
  {
    return adjReason;
  }

  /**
   * Get The tax code associated with the given merchant SKU
   * @return the taxCode
   */
  public String getTaxCode() 
  {
    return taxCode;
  }

  /**
   * Get Image URL for the product
   * @return the url
   */
  public String getUrl() 
  {
    return url;
  }

  /**
   * Get Price adjustment occured as a result of the partial cancel
   * @return the priceAdj
   */
  public Money getPriceAdj() 
  {
    return priceAdj;
  }

  /**
   * Get The fees paid to Jet.com for the given quantity of the given merchant 
   * SKU in the given merchant order
   * @return the fees
   */
  public Money getFees() 
  {
    return fees;
  }

  /**
   * Get The base and variable commissions paid to Jet on this order. Variable 
   * commissions are set through the Rules Engine in the Partner Portal
   * @return the adjustments
   */
  public List<FeeAdjRec> getAdjustments() 
  {
    return adjustments;
  }

  /**
   * Get 
   * @return the taxInfo
   */
  public String getTaxInfo() 
  {
    return taxInfo;
  }

  /**
   * Get 
   * @return the regFees
   */
  public Money getRegFees() 
  {
    return regFees;
  }
  
  
  /**
   * Retrieve the item acknowledged status if available 
   * @return status 
   */
  public ItemAckStatus getItemAckStatus()
  {
    return itemAckStatus;
  }
  
  
  /**
   * Turn this object into jet json 
   * @return json
   */
  public JsonObject toJSON()
  {
    JsonArrayBuilder adj = Json.createArrayBuilder();
    
    for ( FeeAdjRec f : adjustments )
    {
      adj.add( f.toJSON());
    }
    
    return Json.createObjectBuilder()
      .add( "order_item_id", orderItemId )
      .add( "alt_order_item_id", altOrderItemId )
      .add( "merchant_sku", merchantSku )
      .add( "product_title", title )
      .add( "request_order_quantity", requestOrderQty )
      .add( "request_order_cancel_qty", requestOrderCancelQty )
      .add( "adjustment_reason", adjReason )
      .add( "item_tax_code", taxCode )
      .add( "url", url )
      .add( "price_adjustment", priceAdj.asBigDecimal())
      .add( "item_fees", fees.asBigDecimal())
      .add( "tax_info", taxInfo )
      .add( "fee_adjustments", adj.build())
      .add( "regulatory_fees", regFees.asBigDecimal())
      .add( "order_item_acknowledgement_status", itemAckStatus.getText())
      .add( "item_price", itemPrice.toJSON())
      .build();
  }
  
  
  
  @Override
  public String toString()
  {
    return title;
  }
}
