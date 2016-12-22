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
 * Represents an item that is being returned 
 * @author John Quinn
 */
public class ReturnItemRec implements Jsonable
{
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
   * Instance builder
   */
  public static class Builder
  {
    /**
     * Order item id
     */
    private String orderItemId;
    
    /**
     * Alternate item id 
     */
    private String altOrderItemId;
    
    /**
     * Quantity of the given item that was returned 
     */
    private int qtyReturned;
    
    /**
     * Quantity of the given item that was refunded
     */
    private int orderReturnRefundQty;
    
    /**
     * The reason this refund was less than the full amount
     */
    private RefundFeedback feedback;
    
    /**
     * Some notes 
     */
    private String notes;
    
    /**
     * Refund amount 
     */
    private RefundAmountRec amount;    

    
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
     * Quantity of the given item that was returned.
     * @param qtyReturned the qtyReturned to set
     * @return this
     */
    public Builder setQtyReturned( final int qtyReturned )
    {
      if ( qtyReturned < 0 )
      {
        throw new IllegalArgumentException( 
          "qtyReturned cannot be less than zero" );
      }
      
      this.qtyReturned = qtyReturned;
      return this;
    }


    /**
     * Quantity of the given item that was refunded.
     * @param orderReturnRefundQty the orderReturnRefundQty to set
     * @return this
     */
    public Builder setOrderReturnRefundQty( final int orderReturnRefundQty )
    {
      if ( orderReturnRefundQty < 0 )
      {
        throw new IllegalArgumentException( 
          "orderReturnRefundQty cannot be less than zero" );
      }
      
      this.orderReturnRefundQty = orderReturnRefundQty;
      return this;
    }


    /**
     * The reason this refund is less than the full amount.
     * @param feedback the feedback to set
     * @return this
     */
    public Builder setFeedback( final RefundFeedback feedback )
    {
      Utils.checkNull( feedback, "feedback" );
      this.feedback = feedback;
      return this;
    }


    /**
     * Provide additional information about why the item was refunded for 
     * lower than the full amount.
     * @param notes the notes to set
     * @return this
     */
    public Builder setNotes( final String notes )
    {
      Utils.checkNull( notes, "notes" );
      this.notes = notes;
      return this;
    }


    /**
     * The amount the retailer is willing to refund to the customer
     * @param amount the amount to set
     * @return this
     */
    public Builder setAmount( final RefundAmountRec amount )
    {
      Utils.checkNull( amount, "amount" );
      this.amount = amount;
      return this;
    }
    
    
    /**
     * Build the object
     * @return instance
     */
    public ReturnItemRec build()
    {
      return new ReturnItemRec( this );
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
    return new Builder()
      .setAltOrderItemId( from.getAltOrderItemId())
      .setAmount( from.getRefundAmount())
      .setOrderItemId( from.getOrderItemId())
      .setOrderReturnRefundQty( from.getQuantity())
      .setQtyReturned( from.getQuantity());
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
  
    
  /**
   * Convert jet json into this
   * @param json jet json 
   * @return this
   */
  public static ReturnItemRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new Builder()
      .setOrderItemId( json.getString( "order_item_id", "" ))
      .setAltOrderItemId( json.getString( "alt_order_item_id", "" ))
      .setQtyReturned( json.getInt( "total_quantity_returned", 0 ))
      .setOrderReturnRefundQty( json.getInt( "order_return_refund_qty", 0 ))
      .setFeedback( RefundFeedback.fromText( json.getString( "return_refund_feedback", "" )))
      .setNotes( json.getString( "notes", "" ))
      .setAmount( RefundAmountRec.fromJson( json.getJsonObject( "refund_amount" )))
      .build();
  }
    
  
  /**
   * Constructor
   * @param b builder instance 
   */
  private ReturnItemRec( final Builder b )
  {
    this.orderItemId = b.orderItemId;
    this.altOrderItemId = b.altOrderItemId;
    this.qtyReturned = b.qtyReturned;
    this.orderReturnRefundQty = b.orderReturnRefundQty;
    this.feedback = b.feedback;
    this.notes = b.notes;
    this.amount = b.amount;        
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
   * Turn this into jet json
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "order_item_id", orderItemId )
      .add( "alt_order_item_id", altOrderItemId )
      .add( "total_quantity_returned", qtyReturned )
      .add( "order_return_refund_qty", orderReturnRefundQty )
      .add( "return_refund_feedback", feedback.getText())
      .add( "notes", notes )
      .add( "refund_amount", amount.toJSON())
      .build();
  }
}
