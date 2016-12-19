
package com.sheepguru.jetimport.api.jet.orders;

import com.sheepguru.jetimport.api.jet.JetDate;
import com.sheepguru.jetimport.api.jet.Jsonable;
import com.sheepguru.jetimport.api.jet.Utils;


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
  public class Builder
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
      this.merchantOrderId = merchantOrderId;
    }

    /**
     * @param referenceOrderId the referenceOrderId to set
     */
    public void setReferenceOrderId( final String referenceOrderId) 
    {
      this.referenceOrderId = referenceOrderId;
    }

    /**
     * @param customerReferenceOrderId the customerReferenceOrderId to set
     */
    public void setCustomerReferenceOrderId( final String customerReferenceOrderId ) 
    {
      this.customerReferenceOrderId = customerReferenceOrderId;
    }

    /**
     * @param fulfillmentNode the fulfillmentNode to set
     */
    public void setFulfillmentNode( final String fulfillmentNode ) 
    {
      this.fulfillmentNode = fulfillmentNode;
    }

    /**
     * @param altOrderId the altOrderId to set
     */
    public void setAltOrderId( final String altOrderId ) 
    {
      this.altOrderId = altOrderId;
    }

    /**
     * @param hashEmail the hashEmail to set
     */
    public void setHashEmail( final String hashEmail ) 
    {
      this.hashEmail = hashEmail;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(OrderStatus status ) 
    {
      this.status = status;
    }

    /**
     * @param exceptionState the exceptionState to set
     */
    public void setExceptionState( final OrderExceptionState exceptionState ) 
    {
      this.exceptionState = exceptionState;
    }

    /**
     * @param orderPlacedDate the orderPlacedDate to set
     */
    public void setOrderPlacedDate( final JetDate orderPlacedDate ) 
    {
      this.orderPlacedDate = orderPlacedDate;
    }

    /**
     * @param orderTransmissionDate the orderTransmissionDate to set
     */
    public void setOrderTransmissionDate( final JetDate orderTransmissionDate ) 
    {
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
      this.orderDetail = orderDetail;
    }

    /**
     * @param buyer the buyer to set
     */
    public void setBuyer( PersonRec buyer ) 
    {
      this.buyer = buyer;
    }

    
    /**
     * @param shippingTo the shippingTo to set
     */
    public void setShippingTo( final PersonRec shippingTo ) 
    {
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
      return new OrderRec(
        merchantOrderId, 
        referenceOrderId,
        customerReferenceOrderId, 
        fulfillmentNode, 
        altOrderId, 
        hashEmail, 
        status, 
        exceptionState, 
        orderPlacedDate, 
        orderTransmissionDate, 
        jetRequestDirectedCancel,
        orderDetail, 
        buyer, 
        shippingTo, 
        shippingToAddress, 
        orderTotals
      );
    }
  }
  
  
  /**
   * Turn Jet Json into an OrderRec
   * @return object 
   */
  public static OrderRec fromJSON()
  {
    
  }
  
  
  private OrderRec(
    final String merchantOrderId,
    final String referenceOrderId,
    final String customerReferenceOrderId,
    final String fulfillmentNode,
    final String altOrderId,
    final String hashEmail,
    final OrderStatus status,
    final OrderExceptionState exceptionState,
    final JetDate orderPlacedDate,
    final JetDate orderTransmissionDate,
    final boolean jetRequestDirectedCancel,
    final OrderDetailRec orderDetail,
    final PersonRec buyer,
    final PersonRec shippingTo,
    final AddressRec shippingToAddress,
    final OrderTotalRec orderTotals
  ) {
    Utils.checkNull( merchantOrderId, "merchantOrderId" );
    Utils.checkNull( referenceOrderId, "referenceOrderId" );
    Utils.checkNull( customerReferenceOrderId, "customerReferenceOrderId" );
    Utils.checkNull( fulfillmentNode, "fulfillmentNode" );
    Utils.checkNull( altOrderId, "altOrderId" );
    Utils.checkNull( hashEmail, "hashEmail" );
    
    this.merchantOrderId = merchantOrderId;
    this.referenceOrderId = referenceOrderId;
    this.customerReferenceOrderId = customerReferenceOrderId;
    this.fulfillmentNode = fulfillmentNode;
    this.altOrderId = altOrderId;
    this.hashEmail = hashEmail;
    this.status = status;
    this.exceptionState = exceptionState;
    this.orderPlacedDate = orderPlacedDate;
    this.orderTransmissionDate = orderTransmissionDate;
    this.jetRequestDirectedCancel = jetRequestDirectedCancel;
    this.orderDetail = orderDetail;
    this.buyer = buyer;
    this.shippingTo = shippingTo;
    this.shippingToAddress = shippingToAddress;
    this.orderTotals = orderTotals;
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
    
  
}
