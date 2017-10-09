/**
 * This file is part of the Aerodrome package, and is subject to the
 * terms and conditions defined in file 'LICENSE', which is part
 * of this source code package.
 *
 * Copyright (c) 2016 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.buffalokiwi.aerodrome.jet.orders;

import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * I'm sorry about how stupid this is, and how I'm extending a static class.
 * I'll fix it at some point.
 * 
 * @author john
 */
public class RefundItemRec<R extends RefundItemRec, B extends RefundItemRec.Builder> extends ReturnItemRec<R,B>
{
  /**
   * The reason the customer initiated the return.
   */
  private final RefundReason refundReason;
  
  /**
   * A different refund reason in the refund created response from jet 
   */
  private final CreatedRefundReason createdRefundReason;
  
  private final int refundId;
  
  private final String refundAuthId;
  
  
  public static class Builder<T extends Builder, R extends RefundItemRec> extends ReturnItemRec.Builder<T,R>
  {

    /**
     * The reason for the refund
     */
    private RefundReason refundReason = RefundReason.NONE;
    
    private int refundId = 0;
    private String refundAuthId = "";
    

    /**
     * A different refund reason in the refund created response from jet 
     */
    private CreatedRefundReason createdRefundReason = CreatedRefundReason.NONE;

    
    public T setRefundAuthId( final String id )
    {
      Utils.checkNull( id, "id" );
      refundAuthId = id;
      return getReference();
    }
    
    
    public T setCreatedRefundReason( final CreatedRefundReason reason )
    {
      Utils.checkNull( reason, "reason" );
      this.createdRefundReason = reason;
      return getReference();
    }
    
    
    
    @Override
    public T setId( final int id )
    {
      super.setId( id );
      return getReference();
    }    
    
    public String getRefundAuthId()
    {
      return refundAuthId;
    }
    
    public T setRefundId( final int id )
    {
      if ( id < 0 )
        throw new IllegalArgumentException( "id can't be less than zero" );
      this.refundId = id;
      return getReference();
    }
    
   
    /**
     * Jet's unique identifier for an item in a merchant order.
     * @param orderItemId the orderItemId to set
     * @return this
     */
    @Override
    public T setOrderItemId( final String orderItemId )
    {
      super.setOrderItemId( orderItemId );
      return getReference();
    }


    /**
     * If an alt_order_item_id has been associated with the order_item_id via 
     * the order accept message, this may be passed instead of the 
     * order_item_id field.
     * @param altOrderItemId the altOrderItemId to set
     * @return this
     */
    @Override
    public T setAltOrderItemId( final String altOrderItemId )
    {
      super.setAltOrderItemId( altOrderItemId );
      return getReference();
    }


    /**
     * Quantity of the given item that was returned.
     * @param qtyReturned the qtyReturned to set
     * @return this
     */
    @Override
    public T setQtyReturned( final int qtyReturned )
    {
      super.setQtyReturned( qtyReturned );
      return getReference();
    }


    /**
     * Quantity of the given item that was refunded.
     * @param orderReturnRefundQty the orderReturnRefundQty to set
     * @return this
     */
    @Override
    public T setOrderReturnRefundQty( final int orderReturnRefundQty )
    {
      super.setOrderReturnRefundQty( orderReturnRefundQty );
      return getReference();
    }


    /**
     * The reason this refund is less than the full amount.
     * @param feedback the feedback to set
     * @return this
     */
    @Override
    public T setFeedback( final RefundFeedback feedback )
    {
      super.setFeedback( feedback );
      return getReference();
    }


    /**
     * Provide additional information about why the item was refunded for 
     * lower than the full amount.
     * @param notes the notes to set
     * @return this
     */
    @Override
    public T setNotes( final String notes )
    {
      super.setNotes( notes );
      return getReference();
    }


    /**
     * The amount the retailer is willing to refund to the customer
     * @param amount the amount to set
     * @return this
     */
    @Override
    public T setAmount( final RefundAmountRec amount )    
    {
      super.setAmount( amount );
      return getReference();
    }
    
    
    /**
     * The reason the customer initiated the return.
     */
    public T setRefundReason( final RefundReason refundReason )
    {
      Utils.checkNull( refundReason, "refundReason" );
      this.refundReason = refundReason;
      return getReference();
    }
  
    
    /**
     * Build the object
     * @return instance
     */
    @Override
    public R build()
    {
      return (R)new RefundItemRec( Builder.class, this );
    }  

    /**
     * @return the refundReason
     */
    public RefundReason getRefundReason()
    {
      return refundReason;
    }

    /**
     * @return the refundId
     */
    public int getRefundId()
    {
      return refundId;
    }

    /**
     * @return the createdRefundReason
     */
    public CreatedRefundReason getCreatedRefundReason()
    {
      return createdRefundReason;
    }
  }
  
  
  /**
   * Convert jet json into this
   * @param json jet json 
   * @return this
   */
  public static RefundItemRec fromJson( final JsonObject json )
  {
    final ReturnItemRec r = ReturnItemRec.fromJson( json );
    
    final Builder b = new Builder();
    b.setOrderItemId( r.getOrderItemId());
    b.setAltOrderItemId( r.getAltOrderItemId());
    b.setTotalQtyReturned( json.getInt( "total_quantity_returned", 0 ));
    b.setOrderReturnRefundQty( json.getInt( "order_return_refund_qty", 0 ));
    b.setFeedback( RefundFeedback.fromText( json.getString( "refund_feedback", "" )));
    b.setNotes( r.getNotes());
    b.setAmount( r.getAmount());
    
    //..Shared fields with different enum possibilities 
    try {
      b.setCreatedRefundReason( CreatedRefundReason.fromText( json.getString( "refund_reason", "" )));
    } catch( IllegalArgumentException e ) {} //..do nothing
    
    try {
      b.setRefundReason( RefundReason.fromText( json.getString( "refund_reason", "" )));
    } catch( IllegalArgumentException e ) {} //..do nothing
    
    return b.build();
  }  
  
  
  
  /**
   * Convert an order item to a refund item. 
   * This will multiply all of the itemprice amounts by quantity shipped.
   * @param from
   * @return 
   */
  public static RefundItemRec.Builder fromOrderItemRec( final OrderItemRec from )
  {
    Utils.checkNull( from, "from" );
    
    final Builder b = new Builder();
    
    b.setOrderItemId( from.getOrderItemId());
    b.setAltOrderItemId( from.getAltOrderItemId());
    b.setOrderReturnRefundQty( from.getRequestOrderQty());
    b.setQtyReturned( from.getRequestOrderQty());
    b.setAmount( RefundAmountRec.fromItemPriceRec( from.getItemPrice()));
    
    final RefundAmountRec.Builder rb = b.getAmount().toBuilder();
      b.setAmount( rb.setPrincipal( from.getItemPrice().getPrice().times( from.getRequestOrderQty()))
        .setTax( from.getItemPrice().getItemTax().times( from.getRequestOrderQty()))
        .setShippingCost( from.getItemPrice().getShippingPrice().times( from.getRequestOrderQty()))
        .setShippingTax( from.getItemPrice().getItemTax().times( from.getRequestOrderQty())).build());
    
    
    return b;      
  }
  
  
  
  /**
   * Constructor 
   * @param b builder 
   */
  protected RefundItemRec( final Class<? extends Builder> builderClass, final Builder b )
  {
    super( builderClass, b );
    this.refundReason = b.getRefundReason();
    this.refundId = b.getRefundId();
    this.refundAuthId = b.getRefundAuthId();
    this.createdRefundReason = b.getCreatedRefundReason();
  }
  
  
  @Override
  public B toBuilder()
  {
    return (B)super.toBuilder()
    .setRefundReason( this.refundReason )
    .setRefundId( this.refundId )
    .setRefundAuthId( this.refundAuthId )
    .setCreatedRefundReason( this.createdRefundReason );        
  }
  
  
  

  /**
   * The reason for the refund
   * @return reason
   */
  public RefundReason getRefundReason()
  {
    return refundReason;
  }  
  
  
  public CreatedRefundReason getCreatedReason()
  {
    return createdRefundReason;
  }
  
  
  public int getRefundId()
  {
    return refundId;
  }
  
  public String getRefundAuthId()
  {
    return refundAuthId;
  }
  
  
  /**
   * Turn this into jet json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder();
    
    for ( Map.Entry<String,JsonValue> entry : super.toJSON().entrySet())
    {
      if ( entry.getKey().equals( "return_refund_feedback" ))
        b.add( "refund_feedback", entry.getValue());
      else
        b.add( entry.getKey(), entry.getValue());
    }
    
    b.add( "refund_reason", refundReason.getText());
    
    return b.build();
  }  
}
