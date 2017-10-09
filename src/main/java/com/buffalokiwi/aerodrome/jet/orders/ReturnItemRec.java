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

import com.buffalokiwi.aerodrome.jet.BuildableObject;
import com.buffalokiwi.aerodrome.jet.IBuildableObject;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.utils.Money;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * Represents an item that is being returned 
 * @author John Quinn
 */
public class ReturnItemRec<R extends ReturnItemRec, B extends ReturnItemRec.Builder> extends BuildableObject<R,B> implements Jsonable
{
  /**
   * Some non-jet id 
   */
  private final int id;
  
  /**
   * Order item id
   */
  private final String orderItemId;

  /**
   * Alternate item id 
   */
  private final String altOrderItemId;

  /**
   * Quantity of the given item that was returned 
   */
  private final int qtyReturned;
  
  /**
   * Quantity of the given item that was refunded
   */
  private final int orderReturnRefundQty;

  /**
   * The reason this refund was less than the full amount
   */
  private final RefundFeedback feedback;
  
  /**
   * Some notes 
   */
  private final String notes;

  /**
   * Refund amount 
   */
  private final RefundAmountRec amount;  
  
  /**
   * The merchant SKU that is being returned
   */
  private final String merchantSku;
  
  /**
   * The short description of the merchant SKU being returned
   */
  private final String merchantSkuTitle;
  
  /**
   * The reason the customer is returning the item
   */
  private final ReturnReason returnReason;
  
  /**
   * The amount the retailer is willing to refund to the customer
   */
  private final RefundAmountRec requestedRefundAmount;


  
  /**
   * Instance builder
   */
  public static class Builder<T extends Builder, R extends ReturnItemRec> extends BuildableObject.Builder<T,R>
  {
    /**
     * Some non-jet id 
     */
    private int id = 0;
    
    /**
     * Order item id
     */
    private String orderItemId = "";
    
    /**
     * Alternate item id 
     */
    private String altOrderItemId = "";
    
    /**
     * Quantity of the given item that was returned 
     */
    private int qtyReturned = 0;
    
    /**
     * Quantity of the given item that was refunded
     */
    private int orderReturnRefundQty = 0;
    
    /**
     * The reason this refund was less than the full amount
     */
    private RefundFeedback feedback = RefundFeedback.NONE;
    
    /**
     * Some notes 
     */
    private String notes = "";
    
    /**
     * Refund amount 
     */
    private RefundAmountRec amount = null;
    
    /**
     * The merchant SKU that is being returned
     */
    private String merchantSku = "";

    /**
     * The short description of the merchant SKU being returned
     */
    private String merchantSkuTitle = "";

    /**
     * The reason the customer is returning the item
     */
    private ReturnReason returnReason = ReturnReason.NONE;

    /**
     * The amount the retailer is willing to refund to the customer
     */
    private RefundAmountRec requestedRefundAmount = null;
    
    
    public T setId( final int id )
    {
      if ( id < 0 )
        throw new IllegalArgumentException( "id can't be less than zero" );
      
      this.id = id;
      return getReference();
    }
    
    
    /**
     * Jet's unique identifier for an item in a merchant order.
     * @param orderItemId the orderItemId to set
     * @return this
     */
    public T setOrderItemId( final String orderItemId )
    {
      Utils.checkNull( orderItemId, "orderItemId" );
      this.orderItemId = orderItemId;
      return getReference();
    }


    /**
     * If an alt_order_item_id has been associated with the order_item_id via 
     * the order accept message, this may be passed instead of the 
     * order_item_id field.
     * @param altOrderItemId the altOrderItemId to set
     * @return this
     */
    public T setAltOrderItemId( final String altOrderItemId )
    {
      Utils.checkNull( altOrderItemId, "altOrderItemId" );
      this.altOrderItemId = altOrderItemId;
      return getReference();
    }


    /**
     * Quantity of the given item that was returned.
     * @param qtyReturned the qtyReturned to set
     * @return this
     */
    public T setQtyReturned( final int qtyReturned )
    {
      if ( qtyReturned < 0 )
      {
        throw new IllegalArgumentException( 
          "qtyReturned cannot be less than zero" );
      }
      
      this.qtyReturned = qtyReturned;
      return getReference();
    }


    /**
     * Quantity of the given item that was refunded.
     * @param orderReturnRefundQty the orderReturnRefundQty to set
     * @return this
     */
    public T setOrderReturnRefundQty( final int orderReturnRefundQty )
    {
      if ( orderReturnRefundQty < 0 )
      {
        throw new IllegalArgumentException( 
          "orderReturnRefundQty cannot be less than zero" );
      }
      
      this.orderReturnRefundQty = orderReturnRefundQty;
      return getReference();
    }


    /**
     * The reason this refund is less than the full amount.
     * @param feedback the feedback to set
     * @return this
     */
    public T setFeedback( final RefundFeedback feedback )
    {
      Utils.checkNull( feedback, "feedback" );
      this.feedback = feedback;
      return getReference();
    }


    /**
     * Provide additional information about why the item was refunded for 
     * lower than the full amount.
     * @param notes the notes to set
     * @return this
     */
    public T setNotes( final String notes )
    {
      Utils.checkNull( notes, "notes" );
      this.notes = notes;
      return getReference();
    }


    /**
     * The amount the retailer is willing to refund to the customer
     * @param amount the amount to set
     * @return this
     */
    public T setAmount( final RefundAmountRec amount )
    {
      Utils.checkNull( amount, "amount" );
      this.amount = amount;
      return getReference();
    }
    
    public T setTotalQtyReturned( final int qty )
    {
      return setQtyReturned( qty );
    }
    
    
    
    /**
     * Build the object
     * @return instance
     */
    @Override
    public R build()
    {
      if ( this.getAmount() == null )
        this.amount = new RefundAmountRec();
      
      if ( this.getRequestedRefundAmount() == null )
        this.requestedRefundAmount = new RefundAmountRec();
      
      return (R)new ReturnItemRec( Builder.class, this );
    }

    /**
     * @param merchantSku the merchantSku to set
     */
    public T setMerchantSku( String merchantSku )
    {
      Utils.checkNull( merchantSku, "merchantSku" );
      this.merchantSku = merchantSku;
      return getReference();
    }

    /**
     * @param merchantSkuTitle the merchantSkuTitle to set
     */
    public T setMerchantSkuTitle( String merchantSkuTitle )
    {
      Utils.checkNull( merchantSkuTitle, "merchantSkuTitle" );
      this.merchantSkuTitle = merchantSkuTitle;
      return getReference();
    }

    /**
     * @param returnReason the returnReason to set
     */
    public T setReturnReason( ReturnReason returnReason )
    {
      Utils.checkNull( returnReason, "returnReason" );
      this.returnReason = returnReason;
      return getReference();
    }

    /**
     * @param requestedRefundAmount the requestedRefundAmount to set
     */
    public T setRequestedRefundAmount( RefundAmountRec requestedRefundAmount )
    {
      Utils.checkNull( requestedRefundAmount, "requestedRefundAmount" );
      this.requestedRefundAmount = requestedRefundAmount;
      return getReference();
    }

    /**
     * @return the id
     */
    public int getId()
    {
      return id;
    }

    /**
     * @return the orderItemId
     */
    public String getOrderItemId()
    {
      return orderItemId;
    }

    /**
     * @return the altOrderItemId
     */
    public String getAltOrderItemId()
    {
      return altOrderItemId;
    }

    /**
     * @return the qtyReturned
     */
    public int getQtyReturned()
    {
      return qtyReturned;
    }

    /**
     * @return the totalQtyReturned
     */
    public int getTotalQtyReturned()
    {
      return getQtyReturned();
    }

    /**
     * @return the orderReturnRefundQty
     */
    public int getOrderReturnRefundQty()
    {
      return orderReturnRefundQty;
    }

    /**
     * @return the feedback
     */
    public RefundFeedback getFeedback()
    {
      return feedback;
    }

    /**
     * @return the notes
     */
    public String getNotes()
    {
      return notes;
    }

    /**
     * @return the amount
     */
    public RefundAmountRec getAmount()
    {
      return amount;
    }

    /**
     * @return the merchantSku
     */
    public String getMerchantSku()
    {
      return merchantSku;
    }

    /**
     * @return the merchantSkuTitle
     */
    public String getMerchantSkuTitle()
    {
      return merchantSkuTitle;
    }

    /**
     * @return the returnReason
     */
    public ReturnReason getReturnReason()
    {
      return returnReason;
    }

    /**
     * @return the requestedRefundAmount
     */
    public RefundAmountRec getRequestedRefundAmount()
    {
      return requestedRefundAmount;
    }
  } //..Builder
  
  
  /**
   * Convert a ReturnMerchantSkuRec instance into a ReturnItemRec instance.
   * You will still need to fill in a few extra properties.
   * This sets AltOrderItemId, RefundAmount, and OrderItemId and the 2
   * quantity properties are set to from.getQuantity().
   * @param from
   * @return 
   */
  public static Builder fromReturnMerchantSkuRec( final ReturnMerchantSkuRec from )
  {
    return new ReturnItemRec.Builder()
      .setOrderItemId( from.getOrderItemId())
      .setAltOrderItemId( from.getAltOrderItemId())
      .setReturnReason( from.getReason())
      .setRequestedRefundAmount( from.getRefundAmount())
      .setQtyReturned( from.getQuantity())
      .setMerchantSkuTitle( from.getTitle())
      .setMerchantSku( from.getMerchantsku());
  }
  
  
  
  
  
  /**
   * Turn a list of merchant sku's from jet into a list of objects
   * @param json jet json array 
   * @return objects
   */
  public static List<ReturnItemRec> fromJsonArray( final JsonArray json )
  {
    final List<ReturnItemRec> out = new ArrayList<>();
    if ( json != null )
    {
      for ( int i = 0; i < json.size(); i++ )
      {
        out.add( ReturnItemRec.fromJson( json.getJsonObject( i )));
      }
    }
    
    return out;
  }  
  
  
  public static Builder fromJsonToBuilder( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    final Builder b = new Builder()
      .setOrderItemId( json.getString( "order_item_id", "" ))
      .setAltOrderItemId( json.getString( "alt_order_item_id", "" ))
      .setMerchantSku( json.getString( "merchant_sku", "" ))
      .setMerchantSkuTitle( json.getString( "merchant_sku_title", "" ))
      .setReturnReason( ReturnReason.fromText( json.getString(  "reason", "" )))
      .setQtyReturned( json.getInt( "return_quantity", 0 ))
      .setTotalQtyReturned( json.getInt( "total_quantity_returned", 0 ))            
      .setOrderReturnRefundQty( json.getInt( "order_return_refund_qty", 0 ))
      .setFeedback( RefundFeedback.fromText( json.getString( "return_refund_feedback", "" )))
      .setNotes( json.getString( "notes", "" ));
    
    final JsonObject reqAmt = json.getJsonObject( "requested_refund_amount" );
    if ( reqAmt != null )
      b.setRequestedRefundAmount( RefundAmountRec.fromJson( reqAmt ));
    
    final JsonObject refAmt = json.getJsonObject( "refund_amount" );
    if ( refAmt != null )
      b.setAmount( RefundAmountRec.fromJson( refAmt ));

    return b;    
  }
  
    
  /**
   * Convert jet json into this
   * @param json jet json 
   * @return this
   */
  public static ReturnItemRec fromJson( final JsonObject json )
  {
    return fromJsonToBuilder( json ).build();
  }
    
  
  /**
   * Constructor
   * @param b builder instance 
   */
  protected ReturnItemRec( final Class<? extends Builder> builderClass, final Builder b )
  {
    super( builderClass, b );
    if ( b.getAmount() == null )
      this.amount = new RefundAmountRec();
    else
      this.amount = b.getAmount();            
    
    if ( b.getRequestedRefundAmount() == null )
      this.requestedRefundAmount = new RefundAmountRec();
    else
      this.requestedRefundAmount = b.getRequestedRefundAmount();
    
    this.orderItemId = b.getOrderItemId();
    this.altOrderItemId = b.getAltOrderItemId();
    this.qtyReturned = b.getQtyReturned();
    this.orderReturnRefundQty = b.getOrderReturnRefundQty();
    this.feedback = b.getFeedback();
    this.notes = b.getNotes();
    this.merchantSku = b.getMerchantSku();
    this.merchantSkuTitle = b.getMerchantSkuTitle();
    this.returnReason = b.getReturnReason();
    this.id = b.getId();
  }
  
  
  @Override
  public B toBuilder()
  {
    final Builder b = (Builder)super.toBuilder();
    if ( this.amount == null )
      b.amount = new RefundAmountRec();
    else
      b.amount = this.amount;            
    
    if ( this.requestedRefundAmount == null )
      b.requestedRefundAmount = new RefundAmountRec();
    else
      b.requestedRefundAmount = this.requestedRefundAmount;
    
    b.orderItemId = this.orderItemId;
    b.altOrderItemId = this.altOrderItemId;
    b.qtyReturned = this.qtyReturned;
    b.orderReturnRefundQty = this.orderReturnRefundQty;
    b.feedback = this.feedback;
    b.notes = this.notes;
    b.merchantSku = this.merchantSku;
    b.merchantSkuTitle = this.merchantSkuTitle;
    b.returnReason = this.returnReason;
    b.id = this.id;
    
    return (B)b;
  }
  
  
  public int getId()
  {
    return id;
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
   * If an alt_order_item_id has been associated with the order_item_id via 
   * the order accept message, this may be passed instead of the 
   * order_item_id field
   * @return the altOrderItemId
   */
  public String getAltOrderItemId() 
  {
    return altOrderItemId;
  }

  
  /**
   * Quantity of the given item that was returned.
   * @return the qtyReturned
   */
  public int getQtyReturned() 
  {
    return qtyReturned;
  }

  
  /**
   * Quantity of the given item that was returned.
   * @return total
   */
  public int getTotalQtyReturned()
  {
    return getQtyReturned();
  }
  
  
  /**
   * Quantity of the given item that was refunded.
   * @return the orderReturnRefundQty
   */
  public int getOrderReturnRefundQty() 
  {
    return orderReturnRefundQty;
  }
  

  /**
   * The reason this refund is less than the full amount.
   * @return the feedback
   */
  public RefundFeedback getFeedback() 
  {
    return feedback;
  }
  

  /**
   * Provide additional information about why the item was refunded for lower 
   * than the full amount.
   * @return the notes
   */
  public String getNotes() 
  {
    return notes;
  }

  
  /**
   * The amount the retailer is willing to refund to the customer
   * @return the amount
   */
  public RefundAmountRec getAmount() 
  {
    return amount;
  }
  

  /**
   * @return the merchantSku
   */
  public String getMerchantSku()
  {
    return merchantSku;
  }

  /**
   * @return the merchantSkuTitle
   */
  public String getMerchantSkuTitle()
  {
    return merchantSkuTitle;
  }

  /**
   * @return the returnReason
   */
  public ReturnReason getReturnReason()
  {
    return returnReason;
  }

  /**
   * @return the requestedRefundAmount
   */
  public RefundAmountRec getRequestedRefundAmount()
  {
    return requestedRefundAmount;
  }
    
  
  /**
   * Turn this into jet json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "order_item_id", orderItemId )
      .add( "total_quantity_returned", qtyReturned )
      .add( "order_return_refund_qty", orderReturnRefundQty )
      .add( "refund_amount", amount.toJSON());
    
    if ( !altOrderItemId.isEmpty())
      b.add( "alt_order_item_id", altOrderItemId );
    
    if ( feedback != RefundFeedback.NONE )
      b.add( "return_refund_feedback", feedback.getText());
    
    if ( !notes.isEmpty())
      b.add( "notes", notes );
      
    return b.build();
  }
}
