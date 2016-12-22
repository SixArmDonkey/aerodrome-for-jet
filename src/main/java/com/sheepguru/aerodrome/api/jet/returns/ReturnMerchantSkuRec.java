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

package com.sheepguru.aerodrome.api.jet.returns;

import com.sheepguru.aerodrome.api.jet.Jsonable;
import com.sheepguru.aerodrome.api.jet.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Return merchant skus response.
 * 
 * @author John Quinn
 */
public class ReturnMerchantSkuRec implements Jsonable
{
  /**
   * Jet's unique identifier for an item in a merchant order.
   */
  private final String orderItemId;
  
  /**
   * If an alt_order_item_id has been associated with the order_item_id via 
   * the order accept message, this may be passed instead of the 
   * order_item_id field.
   */
  private final String altOrderItemId;
  
  /**
   * The merchant SKU that is being returned
   */
  private final String merchantsku;
  
  /**
   * The short description of the merchant SKU being returned
   */
  private final String title;
  
  /**
   * The reason the customer is returning the item
   */
  private final ReturnReason reason;
  
  /**
   * The quantity of the merchant SKU that is being returned
   */
  private final int quantity;
  
  /**
   * The amount the retailer is willing to refund to the customer
   */
  private final RefundAmountRec refundAmount;


  /**
   * ReturnMerchantRec instance builder 
   */  
  public static class Builder
  {
    /**
     * Jet's unique identifier for an item in a merchant order.
     */
    private String orderItemId = "";
    
    /**
     * If an alt_order_item_id has been associated with the order_item_id via 
     * the order accept message, this may be passed instead of the 
     * order_item_id field.
     */
    private String altOrderItemId = "";
    
    /**
     * The merchant SKU that is being returned
     */
    private String merchantsku = "";
    
    /**
     * The short description of the merchant SKU being returned
     */
    private String title = "";
    
    /**
     * The reason the customer is returning the item
     */
    private ReturnReason reason = ReturnReason.NONE;
    
    /**
     * The quantity of the merchant SKU that is being returned
     */
    private int quantity = 0;
    
    /**
     * The amount the retailer is willing to refund to the customer
     */
    private RefundAmountRec refundAmount = null;

    
    /**
     * Jet's unique identifier for an item in a merchant order.
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
     * If an alt_order_item_id has been associated with the order_item_id via 
     * the order accept message, this may be passed instead of the 
     * order_item_id field.
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
     * The merchant SKU that is being returned
     * @param merchantsku the merchantsku to set
     * @return this
     */
    public Builder setMerchantsku( final String merchantsku ) 
    {
      Utils.checkNull( merchantsku, "merchantsku" );
      this.merchantsku = merchantsku;
      return this;
    }

    /**
     * The short description of the merchant SKU being returned
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
     * The reason the customer is returning the item
     * @param reason the reason to set
     * @return this
     */
    public Builder setReason( final ReturnReason reason ) 
    {
      Utils.checkNull( reason, "reason" );
      this.reason = reason;
      return this;
    }

    /**
     * The quantity of the merchant SKU that is being returned
     * @param quantity the quantity to set
     * @return this
     */
    public Builder setQuantity( final int quantity ) 
    {
      if ( quantity < 0 )
        throw new IllegalArgumentException( "quantity cannot be less than zero" );
      this.quantity = quantity;
      return this;
    }

    /**
     * The amount the retailer is willing to refund to the customer
     * @param refundAmount the refundAmount to set
     * @return this
     */
    public Builder setRefundAmount( final RefundAmountRec refundAmount ) 
    {
      Utils.checkNull( refundAmount, "refundAmount" );
      this.refundAmount = refundAmount;
      return this;
    }


    /**
     * Build an instance 
     * @return object 
     */
    public ReturnMerchantSkuRec build()
    {
      return new ReturnMerchantSkuRec( this );
    }    
  } //..Builder
  
  
  /**
   * Turn a list of merchant sku's from jet into a list of objects
   * @param json jet json array 
   * @return objects
   */
  public static List<ReturnMerchantSkuRec> fromJsonArray( final JsonArray json )
  {
    final List<ReturnMerchantSkuRec> out = new ArrayList<>();
    if ( json != null )
    {
      for ( int i = 0; i < json.size(); i++ )
      {
        out.add( ReturnMerchantSkuRec.fromJson( json.getJsonObject( i )));
      }
    }
    
    return out;
  }
  
  
  
  /**
   * Build an instance from jet json 
   * @param json jet json
   * @return instance 
   */
  public static ReturnMerchantSkuRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final Builder b = new Builder()
      .setOrderItemId( json.getString( "order_item_id", "" ))
      .setAltOrderItemId( json.getString( "alt_order_item_id", "" ))
      .setMerchantsku( json.getString( "merchant_sku", "" ))
      .setTitle( json.getString( "merchant_sku_title", "" ))
      .setReason( ReturnReason.fromText( json.getString( "reason" )))
      .setQuantity( json.getInt( "return_quantity", 0 ));
    
    final JsonObject rAmt = json.getJsonObject( "requested_refund_amount" );
    if ( rAmt != null )    
      b.setRefundAmount( RefundAmountRec.fromJson( rAmt ));
    
    return b.build();
  }
  
  
  
  /**
   * Constructor
   * @param b builder instance 
   */
  private ReturnMerchantSkuRec( final Builder b )
  {
    this.orderItemId = b.orderItemId;
    this.altOrderItemId = b.altOrderItemId;
    this.merchantsku = b.merchantsku;
    this.title = b.title;
    this.reason = b.reason;
    this.quantity = b.quantity;
    this.refundAmount = b.refundAmount;    
  }
  
  

  /**
   * Jet's unique identifier for an item in a merchant order.
   * @return the orderItemId
   */
  public String getOrderItemId() 
  {
    return orderItemId;
  }

  /**
   * If an alt_order_item_id has been associated with the order_item_id via the order accept message, this may be passed instead of the order_item_id field.
   * @return the altOrderItemId
   */
  public String getAltOrderItemId() 
  {
    return altOrderItemId;
  }

  /**
   * The merchant SKU that is being returned
   * @return the merchantsku
   */
  public String getMerchantsku() 
  {
    return merchantsku;
  }

  /**
   * The short description of the merchant SKU being returned
   * @return the title
   */
  public String getTitle() 
  {
    return title;
  }

  /**
   * The reason the customer is returning the item
   * @return the reason
   */
  public ReturnReason getReason() 
  {
    return reason;
  }

  /**
   * The quantity of the merchant SKU that is being returned
   * @return the quantity
   */
  public int getQuantity() 
  {
    return quantity;
  }

  /**
   * The amount the retailer is willing to refund to the customer
   * @return the refundAmount
   */
  public RefundAmountRec getRefundAmount() 
  {
    return refundAmount;
  }  
  
  
  /**
   * Turn this into jet json
   * @return jet json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "order_item_id", orderItemId )
      .add( "alt_order_item_id", altOrderItemId )
      .add( "merchant_sku", merchantsku )
      .add( "merchant_sku_title", title )
      .add( "reason", reason.getText())
      .add( "return_quantity", quantity )
      .add( "requested_refund_amount", refundAmount.toJSON())
      .build();
  }
}
