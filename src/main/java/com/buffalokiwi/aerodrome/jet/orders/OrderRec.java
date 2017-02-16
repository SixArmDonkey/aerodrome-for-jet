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

import com.buffalokiwi.aerodrome.jet.AddressRec;
import com.buffalokiwi.aerodrome.jet.PersonRec;
import com.buffalokiwi.api.APILog;
import com.buffalokiwi.aerodrome.jet.IJetDate;
import com.buffalokiwi.aerodrome.jet.ISO8601UTCDate;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the primary order record for the orders api.
 * @author John Quinn
 * 
 * @todo add additional type checking in here.
 */
public class OrderRec implements Jsonable
{
  /**
   * some non-jet id 
   */
  private final int id;
  
  /**
   * Jet's unique ID for a given merchant order.
   */
  private final String merchantOrderId;
  
  /**
   * Jet's human readable order ID number that may have a small chance of 
   * collision overtime.
   */
  private final String referenceOrderId;
  
  /**
   * Undocumented
   */
  private final String customerReferenceOrderId;
  
  /**
   * The fulfillment node that the order should be shipped from.
   */
  private final String fulfillmentNode;
  
  /**
   * Optional Merchant supplied order ID.If an alt_order_id has been associated 
   * with the merchant_order_id via the order accept message this will be 
   * passed as well.
   */
  private final String altOrderId;
  
  /**
   * The email hash assigned by Jet to be used as the customer email address
   */
  private final String hashEmail;
  
  /**
   * Current status of the order
   * 
   * Status descriptions:
   * 
   *  'created' - order was created by Jet.com but not released for fulfillment
   *  'ready' - order ready to be acknowledged by merchant
   *  'acknowledged' - order was acknowledged by merchant
   *  'inprogress' - one part of the order has been shipped or cancelled
   *  'complete' - all parts of the order have shipped or cancelled
   */
  private final OrderStatus status;
  
  /**
   * Signifies that something unexpected has occurred for this order. For 
   * example, too many units shipped by merchant, merchant failed to accept 
   * order.
   * 
   * Exception State descriptions:
   * 
   *  'exception - too many units cancelled'
   *  'exception - jet manual canceled to complete state'
   *  'exception - too many units shipped'
   *  'exception - order rejected'
   *  'resolved'
   *  
   */
  private final OrderExceptionState exceptionState;
  
  /**
   * The date the merchant order was placed.
   */
  private final IJetDate orderPlacedDate;
  
  /**
   * The date/time the merchant order was sent to the merchant
   */
  private final IJetDate orderTransmissionDate;
  
  /**
   * Determines whether Jet broke the order into shipments 
   * (this should always be false for order-only).
   */
  private final boolean hasShipments;
  
  /**
   * The date the order was switched to 'ready' state
   */
  private final IJetDate orderReadyDate;
  
  /**
   * The date the order was switched to 'acknowledged' state
   */
  private final IJetDate orderAckDate;
  
  /**
   * Status to let Jet know whether you accept or reject the order. Errors 
   * that occur at the item level should be given the status 'rejected - 
   * item level error'. This is returned in the order acknowledgement message.
   */
  private final AckStatus ackStatus;  

  /**
   * A list of shipments to make 
   */
  private final List<ShipmentRec> shipments;
  
  /**
   * This field indicates that one or more items in the order have been 
   * requested to be cancelled by Jet.com. Please see the 
   * request_order_cancel_qty to determine which item and how much of the 
   * item should be cancelled.
   */
  private final boolean jetRequestDirectedCancel;
  
  /**
   * Shipping details about the order
   */
  private final OrderDetailRec orderDetail;
  
  /**
   * Information about the buyer
   */
  private final PersonRec buyer;
  
  /**
   * Information about who the order will be shipped to
   */
  private final PersonRec shippingTo;
  
  /**
   * Information about where the order will be shipped to
   */
  private final AddressRec shippingToAddress;
  
  /**
   * This is an object of order totals. 
   * table below.
   */
  private final OrderTotalRec orderTotals;

  /**
   * A list of order items..
   */
  private final List<OrderItemRec> orderItems;
  
  /**
   * Log instance 
   */
  private static final Log LOG = LogFactory.getLog( OrderRec.class );
  
  
  /**
   * OrderRec builder 
   */
  public static class Builder
  {
    /**
     * Some non jet id 
     */
    private int id;
    
    /**
     * Jet's unique ID for a given merchant order.
     */
    private String merchantOrderId = "";
    
    /**
     * Jet's human readable order ID number that may have a small 
     * chance of collision overtime.
     */
    private String referenceOrderId = "";
    
    /**
     * 
     */
    private String customerReferenceOrderId = "";
    
    /**
     * The fulfillment node that the order should be shipped from.
     */
    private String fulfillmentNode = "";
    
    /**
     * Optional Merchant supplied order ID.If an alt_order_id has been 
     * associated with the merchant_order_id via the order accept message 
     * this will be passed as well.
     */
    private String altOrderId = "";
    
    /**
     * The email hash assigned by Jet to be used as the customer email address
     */
    private String hashEmail = "";
    
    /**
     * Current status of the order
     */
    private OrderStatus status = OrderStatus.NONE;
    
    /**
     * Signifies that something unexpected has occurred for this order. 
     * For example, too many units shipped by merchant, merchant failed 
     * to accept order.
     */
    private OrderExceptionState exceptionState = OrderExceptionState.NONE;
    
    /**
     * The date the merchant order was placed.
     */
    private IJetDate orderPlacedDate = null;
    
    /**
     * The date/time the merchant order was sent to the merchant
     */
    private IJetDate orderTransmissionDate = null;
    
    /**
     * This field indicates that one or more items in the order have been 
     * requested to be cancelled by Jet.com. Please see the 
     * request_order_cancel_qty to determine which item and how much 
     * of the item should be cancelled.
     */
    private boolean jetRequestDirectedCancel = false;
    
    /**
     * Shipping details about the order
     */
    private OrderDetailRec orderDetail = null;
    
    /**
     * Information about the buyer
     */
    private PersonRec buyer = null;
    
    /**
     * Information about who and where the order will be shipped to
     */
    private PersonRec shippingTo = null;
    
    /**
     * Information about who and where the order will be shipped to
     */
    private AddressRec shippingToAddress = null;
    
    /**
     * This is an object of order totals.
     */
    private OrderTotalRec orderTotals = null;

    /**
     * Determines whether Jet broke the order into shipments 
     * (this should always be false for order-only).
     */
    private boolean hasShipments = false;

    /**
     * The date the order was switched to 'ready' state
     */
    private IJetDate orderReadyDate = null;

    /**
     * The date the order was switched to 'acknowledged' state
     */
    private IJetDate orderAckDate = null;

    /**
     * Status to let Jet know whether you accept or reject the order. Errors 
     * that occur at the item level should be given the status 'rejected - 
     * item level error'. This is returned in the order acknowledgement message.
     */
    private AckStatus ackStatus = AckStatus.NONE;

    /**
     * A list of shipments to make 
     */
    private List<ShipmentRec> shipments = new ArrayList<>();
    
    /**
     * A list of order items 
     */
    private List<OrderItemRec> orderItems = new ArrayList<>();
      
    
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
     * Jet's human readable order ID number that may have a small chance of 
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

    public Builder setId( final int id )
    {
      this.id = id;
      return this;
    }
    
    
    /**
     * @param customerReferenceOrderId the customerReferenceOrderId to set
     * @return this
     */
    public Builder setCustomerReferenceOrderId( 
      final String customerReferenceOrderId ) 
    {
      Utils.checkNull( customerReferenceOrderId, "customerReferenceOrderId" );      
      this.customerReferenceOrderId = customerReferenceOrderId;
      return this;
    }

    
    /**
     * The fulfillment node that the order should be shipped from.
     * @param fulfillmentNode the fulfillmentNode to set
     * @return this
     */
    public Builder setFulfillmentNode( final String fulfillmentNode ) 
    {
      Utils.checkNull( fulfillmentNode, "fulfillmentNode" );      
      this.fulfillmentNode = fulfillmentNode;
      return this;
    }

    
    /**
     * Optional Merchant supplied order ID.If an alt_order_id has been 
     * associated with the merchant_order_id via the order accept message 
     * this will be passed as well.
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
     * The email hash assigned by Jet to be used as the customer email address
     * @param hashEmail the hashEmail to set
     * @return this
     */
    public Builder setHashEmail( final String hashEmail ) 
    {
      Utils.checkNull( hashEmail, "hashEmail" );      
      this.hashEmail = hashEmail;
      return this;
    }

    
    /**
     * Current status of the order
     * @param status the status to set
     * @return this
     */
    public Builder setStatus( final OrderStatus status ) 
    {
      Utils.checkNull( status, "status" );
      this.status = status;
      return this;
    }

    
    /**
     * Signifies that something unexpected has occurred for this order. For
     * example, too many units shipped by merchant, merchant failed to accept 
     * order.
     * @param exceptionState the exceptionState to set
     * @return this
     */
    public Builder setExceptionState( final OrderExceptionState exceptionState ) 
    {
      Utils.checkNull( exceptionState, "exceptionState" );
      this.exceptionState = exceptionState;
      return this;
    }

    
    /**
     * The date the merchant order was placed.
     * @param orderPlacedDate the orderPlacedDate to set
     * @return this
     */
    public Builder setOrderPlacedDate( final IJetDate orderPlacedDate ) 
    {
      Utils.checkNull( orderPlacedDate, "orderPlacedDate" );
      this.orderPlacedDate = orderPlacedDate;
      return this;
    }

    
    /**
     * The date/time the merchant order was sent to the merchant
     * @param orderTransmissionDate the orderTransmissionDate to set
     * @return this
     */
    public Builder setOrderTransmissionDate( final IJetDate orderTransmissionDate ) 
    {
      Utils.checkNull( orderTransmissionDate, "orderTransmissionDate" );
      this.orderTransmissionDate = orderTransmissionDate;
      return this;
    }

    
    /**
     * This field indicates that one or more items in the order have been 
     * requested to be cancelled by Jet.com. Please see the 
     * request_order_cancel_qty to determine which item and how much 
     * of the item should be cancelled.
     * @param jetRequestDirectedCancel the jetRequestDirectedCancel to set
     * @return this
     */
    public Builder setJetRequestDirectedCancel( boolean jetRequestDirectedCancel ) 
    {
      this.jetRequestDirectedCancel = jetRequestDirectedCancel;
      return this;
    }

    
    /**
     * Shipping details about the order
     * @param orderDetail the orderDetail to set
     * @return this
     */
    public Builder setOrderDetail( final OrderDetailRec orderDetail ) 
    {
      Utils.checkNull( orderDetail, "orderDetail" );
      this.orderDetail = orderDetail;
      return this;
    }
    

    /**
     * Information about the buyer
     * @param buyer the buyer to set
     * @return this
     */
    public Builder setBuyer( PersonRec buyer ) 
    {
      Utils.checkNull( buyer, "buyer" );
      this.buyer = buyer;
      return this;
    }

    
    /**
     * Information about who and where the order will be shipped to
     * @param shippingTo the shippingTo to set
     * @return this
     */
    public Builder setShippingTo( final PersonRec shippingTo ) 
    {
      Utils.checkNull( shippingTo, "shippingTo" );
      this.shippingTo = shippingTo;
      return this;
    }

    
    /**
     * Information about who and where the order will be shipped to
     * @param shippingToAddress the shippingToAddress to set
     * @return this
     */
    public Builder setShippingToAddress( final AddressRec shippingToAddress )
    {
      this.shippingToAddress = shippingToAddress;
      return this;
    }

    
    /**
     * This is an object of order totals. 
     * @param orderTotals the orderTotals to set
     * @return this
     */
    public Builder setOrderTotals( final OrderTotalRec orderTotals ) 
    {
      this.orderTotals = orderTotals;
      return this;
    }
    
    
    /**
     * Add some items to the order
     * @param orderItems items 
     * @return this 
     */
    public Builder setOrderItems( final List<OrderItemRec> orderItems )
    {
      if ( orderItems == null )
        this.getOrderItems().clear();
      else      
        this.getOrderItems().addAll( orderItems );
      
      return this;
    }
    
    
    /**
     * Build it.
     * @return 
     */
    public OrderRec build()
    {
      return new OrderRec( this );
    }

    /**
     * This is an array of shipments. 
     * @param hasShipments the hasShipments to set
     * @return this
     */
    public Builder setHasShipments( final boolean hasShipments ) 
    {
      this.hasShipments = hasShipments;
      return this;
    }

    /**
     * The date the order was switched to 'ready' state
     * @param orderReadyDate the orderReadyDate to set
     * @return this
     */
    public Builder setOrderReadyDate( final IJetDate orderReadyDate ) 
    {
      this.orderReadyDate = orderReadyDate;
      return this;
    }

    
    /**
     * The date the order was switched to 'acknowledged' state
     * @param orderAckDate the orderAckDate to set
     * @return this
     */
    public Builder setOrderAckDate( final IJetDate orderAckDate ) 
    {      
      this.orderAckDate = orderAckDate;
      return this;
    }

    
    /**
     * Status to let Jet know whether you accept or reject the order. 
     * Errors that occur at the item level should be given the status 
     * 'rejected - item level error'. This is returned in the order 
     * acknowledgement message.
     * @param ackStatus the ackStatus to set
     * @return this
     */
    public Builder setAckStatus( final AckStatus ackStatus )
    {
      Utils.checkNull( ackStatus, "ackStatus" );
      this.ackStatus = ackStatus;
      return this;
    }

    /**
     * @param shipments the shipments to set
     * @return this
     */
    public Builder setShipments( final List<ShipmentRec> shipments) 
    {
      if ( shipments == null )
        this.getShipments().clear();
      else
        this.getShipments().addAll( shipments );
      
      return this;
      /*
      if ( shipments == null )
      {
        this.shipments = null;
        return this;
      }
      
      if ( this.shipments == null )
        this.shipments = new ArrayList<>();
      
      this.shipments.addAll( shipments );
      
      return this;
      */
    }

    /**
     * @return the id
     */
    public int getId()
    {
      return id;
    }

    /**
     * @return the merchantOrderId
     */
    public String getMerchantOrderId()
    {
      return merchantOrderId;
    }

    /**
     * @return the referenceOrderId
     */
    public String getReferenceOrderId()
    {
      return referenceOrderId;
    }

    /**
     * @return the customerReferenceOrderId
     */
    public String getCustomerReferenceOrderId()
    {
      return customerReferenceOrderId;
    }

    /**
     * @return the fulfillmentNode
     */
    public String getFulfillmentNode()
    {
      return fulfillmentNode;
    }

    /**
     * @return the altOrderId
     */
    public String getAltOrderId()
    {
      return altOrderId;
    }

    /**
     * @return the hashEmail
     */
    public String getHashEmail()
    {
      return hashEmail;
    }

    /**
     * @return the status
     */
    public OrderStatus getStatus()
    {
      return status;
    }

    /**
     * @return the exceptionState
     */
    public OrderExceptionState getExceptionState()
    {
      return exceptionState;
    }

    /**
     * @return the orderPlacedDate
     */
    public IJetDate getOrderPlacedDate()
    {
      return orderPlacedDate;
    }

    /**
     * @return the orderTransmissionDate
     */
    public IJetDate getOrderTransmissionDate()
    {
      return orderTransmissionDate;
    }

    /**
     * @return the jetRequestDirectedCancel
     */
    public boolean isJetRequestDirectedCancel()
    {
      return jetRequestDirectedCancel;
    }

    /**
     * @return the orderDetail
     */
    public OrderDetailRec getOrderDetail()
    {
      return orderDetail;
    }

    /**
     * @return the buyer
     */
    public PersonRec getBuyer()
    {
      return buyer;
    }

    /**
     * @return the shippingTo
     */
    public PersonRec getShippingTo()
    {
      return shippingTo;
    }

    /**
     * @return the shippingToAddress
     */
    public AddressRec getShippingToAddress()
    {
      return shippingToAddress;
    }

    /**
     * @return the orderTotals
     */
    public OrderTotalRec getOrderTotals()
    {
      return orderTotals;
    }

    /**
     * @return the hasShipments
     */
    public boolean isHasShipments()
    {
      return hasShipments;
    }

    /**
     * @return the orderReadyDate
     */
    public IJetDate getOrderReadyDate()
    {
      return orderReadyDate;
    }

    /**
     * @return the orderAckDate
     */
    public IJetDate getOrderAckDate()
    {
      return orderAckDate;
    }

    /**
     * @return the ackStatus
     */
    public AckStatus getAckStatus()
    {
      return ackStatus;
    }

    /**
     * @return the shipments
     */
    public List<ShipmentRec> getShipments()
    {
      return shipments;
    }

    /**
     * @return the orderItems
     */
    public List<OrderItemRec> getOrderItems()
    {
      return orderItems;
    }
  }
  
  
  /**
   * Turn Jet Json into an OrderRec
   * @param json Jet json
   * @return object 
   */
  public static OrderRec fromJson( final JsonObject json)
  {
    Utils.checkNull( json, "json" );
    final Builder b = new Builder()
      .setMerchantOrderId( json.getString( "merchant_order_id", "" ))
      .setReferenceOrderId( json.getString( "reference_order_id", "" ))
      .setCustomerReferenceOrderId( json.getString( "customer_reference_order_id", "" ))
      .setFulfillmentNode( json.getString( "fulfillment_node", "" ))
      .setAltOrderId( json.getString( "alt_order_id", "" ))
      .setHashEmail( json.getString( "hash_email", "" ))
      .setStatus( OrderStatus.fromText( json.getString( "status", "" )))
      .setExceptionState( OrderExceptionState.fromText( json.getString( "exception_state", "" )))
      .setOrderPlacedDate(new ISO8601UTCDate( json.getString( "order_placed_date", "" )))
      .setOrderTransmissionDate(new ISO8601UTCDate( json.getString( "order_transmission_date", "" )))
      .setJetRequestDirectedCancel( json.getBoolean( "jet_requested_directed_cancel", false ))
      ;
    
    buildOrderDetail( b, json.getJsonObject( "order_detail" ));    
    buildBuyer( b, json.getJsonObject( "buyer" ));
    buildShipTo( b, json.getJsonObject( "shipping_to" ));
    buildOrderTotals( b, json.getJsonObject( "order_totals" ));
    
    try {
      buildOrderItems( b, json.getJsonArray( "order_items" ));
    } catch( JetException e ) {
      APILog.error( LOG, e, "Failed to generate order items" );
    }
    
    
    //..Build the shipments
    final JsonArray shipments = json.getJsonArray( "shipments" );
    if ( shipments != null )
    {
      for ( int i = 0; i < shipments.size(); i++ )
      {
        final JsonObject shipment = shipments.getJsonObject( i );
        if ( shipment == null )
          continue;
        
        b.getShipments().add( ShipmentRec.fromJson( shipment ));
      }
    }
    
    return new OrderRec( b );
  }
  
  
  private static void buildOrderItems( final Builder b, final JsonArray json )
    throws JetException
  {
    if ( json == null )
      return;
    
    final List<OrderItemRec> l = new ArrayList<>();
    
    for ( int i = 0; i < json.size(); i++ )
    {
      l.add( OrderItemRec.fromJson( json.getJsonObject( i )));
    }
    
    b.setOrderItems( l );
  }
  
  
  /**
   * Build the order totals object from jet json.
   * @param b builder
   * @param json json 
   */
  private static void buildOrderTotals( final Builder b, final JsonObject json )
  {
    if ( json == null )
      return;
    
    b.setOrderTotals( OrderTotalRec.fromJson( json ));
  }
  
  
  /**
   * Build the ship to objects from jet json 
   * @param b builder
   * @param json json 
   */
  private static void buildShipTo( final Builder b, final JsonObject json )
  {
    if ( json == null )
      return;
    
    final JsonObject r = json.getJsonObject( "recipient" );
    final JsonObject s = json.getJsonObject( "address" );
    
    if ( r == null )
    {
      throw new IllegalArgumentException( 
        "missing recipient property for shipping_to" );
    }
    
    if ( s == null )      
    {
      throw new IllegalArgumentException( 
        "missing address property for shipping_to" );
    }
    
    b.setShippingTo( PersonRec.fromJson( r ));    
    b.setShippingToAddress( AddressRec.fromJson( s ));    
  }
  
  
  
  /**
   * Build the buyer object from jet json 
   * @param b builder
   * @param json jet json 
   */
  private static void buildBuyer( final Builder b, final JsonObject json )
  {
    if ( json == null )
      return;
    
    b.setBuyer( PersonRec.fromJson( json ));
  }
  
  
  /**
   * Build order detail object from jet json
   * @param b builder
   * @param json jet json 
   */
  private static void buildOrderDetail( final Builder b, final JsonObject json )
  {
    if ( json == null )
      return;
    
    b.setOrderDetail( OrderDetailRec.fromJson( json ));
  }
  
  
  /**
   * Create a new OrderRec instance from the builder
   * @param b Builder
   */
  private OrderRec( final Builder b )
  {
    this.merchantOrderId = b.getMerchantOrderId();
    this.referenceOrderId = b.getReferenceOrderId();
    this.customerReferenceOrderId = b.getCustomerReferenceOrderId();
    this.fulfillmentNode = b.getFulfillmentNode();
    this.altOrderId = b.getAltOrderId();
    this.hashEmail = b.getHashEmail();
    this.status = b.getStatus();
    this.exceptionState = b.getExceptionState();
    this.orderPlacedDate = b.getOrderPlacedDate();
    this.orderTransmissionDate = b.getOrderTransmissionDate();
    this.jetRequestDirectedCancel = b.isJetRequestDirectedCancel();
    this.orderDetail = b.getOrderDetail();
    this.buyer = b.getBuyer();
    this.shippingTo = b.getShippingTo();
    this.shippingToAddress = b.getShippingToAddress();
    this.orderTotals = b.getOrderTotals();
    this.hasShipments = b.isHasShipments();
    this.orderReadyDate = b.getOrderReadyDate();
    this.orderAckDate = b.getOrderAckDate();    
    this.ackStatus = b.getAckStatus();
    this.shipments = b.getShipments();
    this.orderItems = b.getOrderItems();
    this.id = b.getId();
  }
  
  
  
  public Builder toBuilder()
  {
    final Builder b = new Builder();    
    b.merchantOrderId = this.getMerchantOrderId();
    b.referenceOrderId = this.getReferenceOrderId();
    b.customerReferenceOrderId = this.getCustomerReferenceOrderId();
    b.fulfillmentNode = this.getFulfillmentNode();
    b.altOrderId = this.getAltOrderId();
    b.hashEmail = this.getHashEmail();
    b.status = this.getStatus();
    b.exceptionState = this.getExceptionState();
    b.orderPlacedDate = this.getOrderPlacedDate();
    b.orderTransmissionDate = this.getOrderTransmissionDate();
    b.jetRequestDirectedCancel = this.isJetRequestDirectedCancel();
    b.orderDetail = this.getOrderDetail();
    b.buyer = this.getBuyer();
    b.shippingTo = this.getShippingTo();
    b.shippingToAddress = this.getShippingToAddress();
    b.orderTotals = this.getOrderTotals();
    b.hasShipments = this.hasShipments();
    b.orderReadyDate = this.getOrderReadyDate();
    b.orderAckDate = this.getOrderAckDate();    
    b.ackStatus = this.getAckStatus();
    b.shipments = this.getShipments();
    b.orderItems = this.getOrderItems();
    b.id = this.getId();    
    
    return b;
  }
  
  
  public int getId()
  {
    return id;
  }
  
  
  /**
   * Get Jet's unique ID for a given merchant order.
   * @return the merchantOrderId
   */
  public String getMerchantOrderId() 
  {
    return merchantOrderId;
  }

  
  /**
   * Get Jet's human readable order ID number that may have a small chance of 
   * collision overtime.
   * @return the referenceOrderId
   */
  public String getReferenceOrderId() {
    return referenceOrderId;
  }

  
  /**
   * Undocumented.
   * @return the customerReferenceOrderId
   */
  public String getCustomerReferenceOrderId() {
    return customerReferenceOrderId;
  }

  
  /**
   * Get The fulfillment node that the order should be shipped from.
   * @return the fulfillmentNode
   */
  public String getFulfillmentNode() {
    return fulfillmentNode;
  }

  
  /**
   * Get Optional Merchant supplied order ID.If an alt_order_id has been 
   * associated with the merchant_order_id via the order accept message 
   * this will be passed as well.
   * @return the altOrderId
   */
  public String getAltOrderId() {
    return altOrderId;
  }

  
  /**
   * Get The email hash assigned by Jet to be used as the customer email address
   * @return the hashEmail
   */
  public String getHashEmail() {
    return hashEmail;
  }

  
  /**
   * Current status of the order
   * @return the status
   */
  public OrderStatus getStatus() {
    return status;
  }

  
  /**
   * Signifies that something unexpected has occurred for this order. For 
   * example, too many units shipped by merchant, merchant failed to accept 
   * order.
   * @return the exceptionState
   */
  public OrderExceptionState getExceptionState() {
    return exceptionState;
  }
  

  /**
   * The date the merchant order was placed.
   * @return the orderPlacedDate
   */
  public IJetDate getOrderPlacedDate() {
    return orderPlacedDate;
  }

  
  /**
   * The date/time the merchant order was sent to the merchant
   * @return the orderTransmissionDate
   */
  public IJetDate getOrderTransmissionDate() {
    return orderTransmissionDate;
  }
  

  /**
   * This field indicates that one or more items in the order have been 
   * requested to be cancelled by Jet.com. Please see the 
   * request_order_cancel_qty to determine which item and how much of the 
   * item should be cancelled.
   * @return the jetRequestDirectedCancel
   */
  public boolean isJetRequestDirectedCancel() {
    return jetRequestDirectedCancel;
  }

  
  /**
   * Shipping details about the order
   * @return the orderDetail
   */
  public OrderDetailRec getOrderDetail() {
    return orderDetail;
  }

  
  /**
   * Information about the buyer
   * @return the buyer
   */
  public PersonRec getBuyer() {
    return buyer;
  }

  
  /**
   * Information about who the order will be shipped to
   * @return the shippingTo
   */
  public PersonRec getShippingTo() {
    return shippingTo;
  }

  
  /**
   * Information about where the order will be shipped to
   * @return the shippingToAddress
   */
  public AddressRec getShippingToAddress() {
    return shippingToAddress;
  }

  
  /**
   * This is an object of order totals. 
   * @return the orderTotals
   */
  public OrderTotalRec getOrderTotals() {
    return orderTotals;
  }
  
  
  /**
   * Retrieve the order items list 
   * @return list
   */
  public List<OrderItemRec> getOrderItems()
  {
    return orderItems;
  }
//////////////////////////
  
  /**
   * Determines whether Jet broke the order into shipments 
   * (this should always be false for order-only).
   * @return the hasShipments
   */
  public boolean hasShipments() {
    return hasShipments;
  }

  /**
   * The date the order was switched to 'ready' state
   * @return the orderReadyDate
   */
  public IJetDate getOrderReadyDate() {
    return orderReadyDate;
  }

  /**
   * The date the order was switched to 'acknowledged' state
   * @return the orderAckDate
   */
  public IJetDate getOrderAckDate() {
    return orderAckDate;
  }

  /**
   * Status to let Jet know whether you accept or reject the order. Errors 
   * that occur at the item level should be given the status 'rejected - 
   * item level error'. This is returned in the order acknowledgement message.
   * @return the ackStatus
   */
  public AckStatus getAckStatus() {
    return ackStatus;
  }

  /**
   * Get list of shipments to make 
   * @return the shipments
   */
  public List<ShipmentRec> getShipments() {
    return shipments;
  }
  

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode( this.merchantOrderId );
    return hash;
  }  
  

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    final OrderRec other = (OrderRec) obj;
    return Objects.equals( this.merchantOrderId, other.merchantOrderId );
  }
  
  

  
  /**
   * Turn this object into Jet json 
   * @return json 
   */
  @Override
  public JsonObject toJSON()
  {
    final JsonObject shipTo = Json.createObjectBuilder()
      .add( "recipient", shippingTo.toJSON())
      .add( "address", shippingToAddress.toJSON())
      .build();
    
    
    final JsonObjectBuilder b = Json.createObjectBuilder()
     .add( "merchant_order_id", merchantOrderId )
     .add( "reference_order_id", referenceOrderId )
     .add( "customer_reference_order_id", customerReferenceOrderId )
     .add( "fulfillment_node", fulfillmentNode )
     .add( "alt_order_id", altOrderId )
     .add( "hash_email", hashEmail )
     .add( "status", status.getText())
     .add( "exception_state", exceptionState.getText())
     .add( "order_placed_date", orderPlacedDate.getDateString())
     .add( "order_transmission_date", orderTransmissionDate.getDateString())
     .add( "jet_request_directed_cancel", (( jetRequestDirectedCancel ) ? "true" : "false" ))
     .add( "order_detail", orderDetail.toJSON())
     .add( "buyer", buyer.toJSON())
     .add( "shipping_to", shipTo )
     .add( "order_totals", orderTotals.toJSON())
     .add( "has_shipments", hasShipments )
     .add( "acknowledgement_status", ackStatus.getText());
    
    if ( orderReadyDate != null )
      b.add( "order_ready_date", orderReadyDate.getDateString());
    
    if ( orderAckDate != null )
      b.add( "order_acknowledge_date", orderAckDate.getDateString());
    
    if ( shipments != null )
      b.add( "shipments", shipmentsToJson());
    
    if ( orderItems != null )
      b.add( "order_items", orderItemsToJson());
            
    return b.build();
  }  
  
  
  public List<RefundItemRec.Builder> generateItemsForRefund()
  {
    //..a list of items to refund 
    final List<RefundItemRec.Builder> itemsToRefund = new ArrayList<>();

    //..Convert each order item to a refund item and add it to the list 
    for ( final OrderItemRec item : orderItems )
    {
      itemsToRefund.add( RefundItemRec.fromOrderItemRec( item )
        .setFeedback( RefundFeedback.OPENED )
        .setNotes( "Some notes about the refund" ));
    }      
    
    return itemsToRefund;    
  }
  
  
  /**
   * Turn the shipments array into a JsonArray 
   * @return 
   */
  private JsonArray shipmentsToJson()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    
    if ( shipments != null )
    {
      for ( final ShipmentRec s : shipments )
      {
        ab.add( s.toJSON());
      }
    }
    
    return ab.build();
    
  }
  
  
  /**
   * Turn the shipments array into a JsonArray 
   * @return 
   */
  private JsonArray orderItemsToJson()
  {
    final JsonArrayBuilder ab = Json.createArrayBuilder();
    
    if ( orderItems != null )
    {
      for ( final OrderItemRec o : orderItems )
      {
        ab.add( o.toJSON());
      }
    }
    
    return ab.build();
    
  }  
  
  
  @Override
  public String toString()
  {
    return merchantOrderId;
  }
}