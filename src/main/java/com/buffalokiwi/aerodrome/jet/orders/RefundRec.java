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

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.aerodrome.jet.orders.ItemPriceRec;
import com.buffalokiwi.aerodrome.jet.orders.OrderItemRec;
import com.buffalokiwi.aerodrome.jet.orders.OrderRec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Represents a refund 
 * @author John Quinn
 */
public class RefundRec implements Jsonable
{
  /**
   * RefundRec builder 
   */
  public static class Builder
  {
    /**
     * Jet's human readable return authorization number that may have a small 
     * chance of collision overtime.
     */
    private String refundAuthId = "";
    
    /**
     * The refund ID in the merchant's system associated with this refund.
     */
    private String altRefundId = "";
    
    /**
     * Jet's return message for whether the refund was accepted
     */
    private RefundAcceptedStatus status = RefundAcceptedStatus.NONE;
    
    /**
     * Jet's unique ID for a given merchant order.
     */
    private String merchantOrderId = "";
    
    /**
     * Jet's human readable merchant order ID that may have a small chance 
     * of collision overtime.
     */
    private String referenceOrderId = "";
    
    /**
     * Optional merchant supplied order ID.
     */
    private String altOrderId = "";
    
    /**
     * Items refunded in the order 
     */
    private List<RefundItemRec> items = new ArrayList<>();    

    
    /**
     * Jet's human readable return authorization number that may have a small 
     * chance of collision overtime.
     * @param refundAuthId the refundAuthId to set
     * @return this
     */
    public Builder setRefundAuthId( final String refundAuthId )
    {
      Utils.checkNull( refundAuthId, "refundAuthId" );
      this.refundAuthId = refundAuthId;
      return this;
    }

    
    /**
     * The refund ID in the merchant's system associated with this refund.
     * @param altRefundId the altRefundId to set
     * @return this
     */
    public Builder setAltRefundId( final String altRefundId )
    {
      Utils.checkNull( altRefundId, "altRefundId" );
      this.altRefundId = altRefundId;
      return this;
    }

    
    /**
     * Jet's return message for whether the refund was accepted
     * @param status the status to set
     * @return this
     */
    public Builder setStatus( final RefundAcceptedStatus status )
    {
      Utils.checkNull( status, "status" );
      this.status = status;
      return this;
    }
    

    /**
     * Jet's unique ID for a given merchant order.
     * @param merchantOrderId the merchantOrderId to set
     * @return this
     */
    public Builder setMerchantOrderId( final String merchantOrderId )
    {
      Utils.checkNull( merchantOrderId, "merchantOrderId" );
      this.merchantOrderId = merchantOrderId;
      return this;
    }

    
    /**
     * Jet's human readable merchant order ID that may have a small chance of 
     * collision overtime.
     * @param referenceOrderId the referenceOrderId to set
     * @return this
     */
    public Builder setReferenceOrderId( final String referenceOrderId )
    {
      Utils.checkNull( referenceOrderId, "referenceOrderId" );
      this.referenceOrderId = referenceOrderId;
      return this;
    }

    
    /**
     * Optional merchant supplied order ID.
     * @param altOrderId the altOrderId to set
     * @return this
     */
    public Builder setAltOrderId( final String altOrderId )
    {
      Utils.checkNull( altOrderId, "altOrderId" );
      this.altOrderId = altOrderId;
      return this;
    }

    
    /**
     * Set items in refund 
     * @param items the items to set
     * @return this
     */
    public Builder setItems( final List<RefundItemRec> items )
    {
      if ( items == null )
        this.items.clear();
      else
        this.items.addAll( items );
      
      return this;
    }
    
    
    /**
     * Build the object
     * @return instance
     */
    public RefundRec build()
    {
      return new RefundRec( this );
    }
  } //..Builder 
  
  
  public static Builder fromOrderRec( final OrderRec order )
  {
    Utils.checkNull( order, "order" );
    
    //..a list of items to refund 
    final List<RefundItemRec> itemsToRefund = new ArrayList<>();

    //..Convert each order item to a refund item and add it to the list 
    for ( final OrderItemRec item : order.getOrderItems())
    {
      itemsToRefund.add( RefundItemRec.fromOrderItemRec( item )
        .setFeedback( RefundFeedback.OPENED )
        .setNotes( "Some notes about the refund" )
        .build());
    }

    return new RefundRec.Builder()
      .setAltOrderId( order.getAltOrderId())
      .setItems( itemsToRefund )
      .setMerchantOrderId( order.getMerchantOrderId())
      .setReferenceOrderId( order.getReferenceOrderId());
  }
  
  
  
  
  /**
   * Jet's human readable return authorization number that may have a small 
   * chance of collision overtime.
   */
  private final String refundAuthId;
  
  /**
   * The refund ID in the merchant's system associated with this refund.
   */
  private final String altRefundId;
  
  /**
   * Jet's return message for whether the refund was accepted
   */
  private final RefundAcceptedStatus status;
  
  /**
   * Jet's unique ID for a given merchant order.
   */
  private final String merchantOrderId;
  
  /**
   * Jet's human readable merchant order ID that may have a small chance of 
   * collision overtime.
   */
  private final String referenceOrderId;
  
  /**
   * Optional merchant supplied order ID.
   */
  private final String altOrderId;
  
  /**
   * Items in the refund 
   */
  private final List<RefundItemRec> items;
  
  
  
  /**
   * Convert jet json into an instance of this 
   * @param json json 
   * @return instance 
   */
  public static RefundRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new Builder()
      .setRefundAuthId( json.getString( "refund_authorization_id", "" ))
      .setAltOrderId( json.getString( "alt_refund_id", "" ))
      .setStatus(  RefundAcceptedStatus.fromText( json.getString( "refund_status", "" )))
      .setMerchantOrderId( json.getString( "merchant_order_id", "" ))
      .setReferenceOrderId( json.getString( "reference_order_id" ))
      .setAltOrderId( json.getString( "alt_order_id", "" ))
      .setItems( jsonToItems( json.getJsonArray( "items" )))
      .build();      
  }
  
  
  /**
   * Turn some json into RefundItemRec instances 
   * @param a json 
   * @return list
   */
  private static List<RefundItemRec> jsonToItems( final JsonArray a )
  {
    final List<RefundItemRec> out = new ArrayList<>();
    
    if ( a == null )
      return out;
    
    for ( int i = 0; i < a.size(); i++ )
    {
      out.add( RefundItemRec.fromJson( a.getJsonObject( i )));
    }
    
    return out;
  }
  
  
  /**
   * A refund record 
   * @param b builder
   */
  protected RefundRec( final Builder b )
  {
    Utils.checkNull( b, "b" );
    
    this.refundAuthId = b.refundAuthId;
    this.altRefundId = b.altRefundId;
    this.status = b.status;
    this.merchantOrderId = b.merchantOrderId;
    this.referenceOrderId = b.referenceOrderId;
    this.altOrderId = b.altOrderId;
    this.items = Collections.unmodifiableList( b.items );
  }
  
  
  /**
   * Jet's human readable return authorization number that may have a small 
   * chance of collision overtime.
   * @return the refundAuthId
   */
  public String getRefundAuthId()
  {
    return refundAuthId;
  }

  
  /**
   * The refund ID in the merchant's system associated with this refund.
   * @return the altRefundId
   */
  public String getAltRefundId()
  {
    return altRefundId;
  }

  
  /**
   * Jet's return message for whether the refund was accepted
   * @return the status
   */
  public RefundAcceptedStatus getStatus()
  {
    return status;
  }

  
  /**
   * Jet's unique ID for a given merchant order.
   * @return the merchantOrderId
   */
  public String getMerchantOrderId()
  {
    return merchantOrderId;
  }

  
  /**
   * Jet's human readable merchant order ID that may have a small chance of
   * collision overtime.
   * @return the referenceOrderId
   */
  public String getReferenceOrderId()
  {
    return referenceOrderId;
  }

  
  /**
   * Optional merchant supplied order ID.
   * @return the altOrderId
   */
  public String getAltOrderId()
  {
    return altOrderId;
  }
  

  /**
   * Items in the refund 
   * @return the items
   */
  public List<RefundItemRec> getItems()
  {
    return items;
  }
  
  
  /**
   * Turn this into jet json 
   * @return json
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObjectBuilder b = Json.createObjectBuilder()
      .add( "refund_authorization_id", refundAuthId )
      .add(  "alt_refund_id", altRefundId )
      .add( "refund_status", status.getText())
      .add( "merchant_order_id", merchantOrderId )
      .add( "reference_order_id", referenceOrderId )
      .add( "alt_order_id", altOrderId );
    
    if ( !items.isEmpty())
      b.add( "items", Utils.<RefundItemRec>jsonableToArray( items ));
    
    return b.build();
  }
}
