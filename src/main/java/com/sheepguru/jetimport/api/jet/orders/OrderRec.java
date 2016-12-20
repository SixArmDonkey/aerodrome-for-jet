
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.JetDate;
import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;


/**
 * This is the primary order record for the orders api.
 * @author John Quinn
 */
public class OrderRec implements Jsonable
{
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
   * 
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
  private final JetDate orderPlacedDate;
  
  /**
   * The date/time the merchant order was sent to the merchant
   */
  private final JetDate orderTransmissionDate;
  
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
   * OrderRec builder 
   */
  public static class Builder
  {
    private String merchantOrderId = "";
    private String referenceOrderId = "";
    private String customerReferenceOrderId = "";
    private String fulfillmentNode = "";
    private String altOrderId = "";
    private String hashEmail = "";
    private OrderStatus status = null;
    private OrderExceptionState exceptionState = null;
    private JetDate orderPlacedDate = null;
    private JetDate orderTransmissionDate = null;
    private boolean jetRequestDirectedCancel = false;
    private OrderDetailRec orderDetail = null;
    private PersonRec buyer = null;
    private PersonRec shippingTo = null;
    private AddressRec shippingToAddress = null;
    private OrderTotalRec orderTotals = null;

    /**
     * @param merchantOrderId the merchantOrderId to set
     */
    public void setMerchantOrderId( final String merchantOrderId) 
    {
      Utils.checkNull( merchantOrderId, "merchantOrderId" );      
      this.merchantOrderId = merchantOrderId;
    }

    /**
     * @param referenceOrderId the referenceOrderId to set
     */
    public void setReferenceOrderId( final String referenceOrderId) 
    {
      Utils.checkNull( referenceOrderId, "referenceOrderId" );      
      this.referenceOrderId = referenceOrderId;
    }

    /**
     * @param customerReferenceOrderId the customerReferenceOrderId to set
     */
    public void setCustomerReferenceOrderId( final String customerReferenceOrderId ) 
    {
      Utils.checkNull( customerReferenceOrderId, "customerReferenceOrderId" );      
      this.customerReferenceOrderId = customerReferenceOrderId;
    }

    /**
     * @param fulfillmentNode the fulfillmentNode to set
     */
    public void setFulfillmentNode( final String fulfillmentNode ) 
    {
      Utils.checkNull( fulfillmentNode, "fulfillmentNode" );      
      this.fulfillmentNode = fulfillmentNode;
    }

    /**
     * @param altOrderId the altOrderId to set
     */
    public void setAltOrderId( final String altOrderId ) 
    {
      Utils.checkNull( altOrderId, "altOrderId" );
      this.altOrderId = altOrderId;
    }

    /**
     * @param hashEmail the hashEmail to set
     */
    public void setHashEmail( final String hashEmail ) 
    {
      Utils.checkNull( hashEmail, "hashEmail" );      
      this.hashEmail = hashEmail;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(OrderStatus status ) 
    {
      Utils.checkNull( status, "status" );
      this.status = status;
    }

    /**
     * @param exceptionState the exceptionState to set
     */
    public void setExceptionState( final OrderExceptionState exceptionState ) 
    {
      Utils.checkNull( exceptionState, "exceptionState" );
      this.exceptionState = exceptionState;
    }

    /**
     * @param orderPlacedDate the orderPlacedDate to set
     */
    public void setOrderPlacedDate( final JetDate orderPlacedDate ) 
    {
      Utils.checkNull( orderPlacedDate, "orderPlacedDate" );
      this.orderPlacedDate = orderPlacedDate;
    }

    /**
     * @param orderTransmissionDate the orderTransmissionDate to set
     */
    public void setOrderTransmissionDate( final JetDate orderTransmissionDate ) 
    {
      Utils.checkNull( orderTransmissionDate, "orderTransmissionDate" );
      this.orderTransmissionDate = orderTransmissionDate;
    }

    /**
     * @param jetRequestDirectedCancel the jetRequestDirectedCancel to set
     */
    public void setJetRequestDirectedCancel( boolean jetRequestDirectedCancel ) 
    {
      this.jetRequestDirectedCancel = jetRequestDirectedCancel;
    }

    /**
     * @param orderDetail the orderDetail to set
     */
    public void setOrderDetail( final OrderDetailRec orderDetail ) 
    {
      Utils.checkNull( orderDetail, "orderDetail" );
      this.orderDetail = orderDetail;
    }

    /**
     * @param buyer the buyer to set
     */
    public void setBuyer( PersonRec buyer ) 
    {
      Utils.checkNull( buyer, "buyer" );
      this.buyer = buyer;
    }

    
    /**
     * @param shippingTo the shippingTo to set
     */
    public void setShippingTo( final PersonRec shippingTo ) 
    {
      Utils.checkNull( shippingTo, "shippingTo" );
      this.shippingTo = shippingTo;
    }

    
    /**
     * @param shippingToAddress the shippingToAddress to set
     */
    public void setShippingToAddress( final AddressRec shippingToAddress )
    {
      this.shippingToAddress = shippingToAddress;
    }

    
    /**
     * @param orderTotals the orderTotals to set
     */
    public void setOrderTotals( final OrderTotalRec orderTotals ) 
    {
      this.orderTotals = orderTotals;
    }
    
    
    /**
     * Build it.
     * @return 
     */
    public OrderRec build()
    {
      return new OrderRec( this );
    }
  }
  
  
  /**
   * Turn Jet Json into an OrderRec
   * @return object 
   */
  public static OrderRec fromJSON()
  {
    return null;
  }
  
  
  /**
   * Create a new OrderRec instance from the builder
   * @param b Builder
   */
  private OrderRec( final Builder b )
  {
    this.merchantOrderId = b.merchantOrderId;
    this.referenceOrderId = b.referenceOrderId;
    this.customerReferenceOrderId = b.customerReferenceOrderId;
    this.fulfillmentNode = b.fulfillmentNode;
    this.altOrderId = b.altOrderId;
    this.hashEmail = b.hashEmail;
    this.status = b.status;
    this.exceptionState = b.exceptionState;
    this.orderPlacedDate = b.orderPlacedDate;
    this.orderTransmissionDate = b.orderTransmissionDate;
    this.jetRequestDirectedCancel = b.jetRequestDirectedCancel;
    this.orderDetail = b.orderDetail;
    this.buyer = b.buyer;
    this.shippingTo = b.shippingTo;
    this.shippingToAddress = b.shippingToAddress;
    this.orderTotals = b.orderTotals;
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
  public JetDate getOrderPlacedDate() {
    return orderPlacedDate;
  }

  /**
   * The date/time the merchant order was sent to the merchant
   * @return the orderTransmissionDate
   */
  public JetDate getOrderTransmissionDate() {
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
    
    
    return Json.createObjectBuilder()
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
     .build();
  }  
}