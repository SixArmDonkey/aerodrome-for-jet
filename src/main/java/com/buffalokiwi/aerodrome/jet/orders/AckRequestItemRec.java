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

import com.buffalokiwi.aerodrome.jet.Jsonable;
import com.buffalokiwi.aerodrome.jet.Utils;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * An item to be added to the send order acknowledgement command.
 * @author John Quinn
 */
public class AckRequestItemRec implements Jsonable
{
  /**
   * Merchant defined fulfillable or nonfulfillable skus within the order.
   * 
   * nonfulfillable - invalid merchant SKU
   * nonfulfillable - no inventory
   * fulfillable
   */
  public static enum Status {
    NONE( "" ),
    FULFILLABLE( "fulfillable" ),
    NO_INVENTORY( "nonfulfillable - no inventory" ),
    INVALID_SKU( "nonfulfillable - invalid merchant SKU" );

    private final String text;

    private static final Status[] values = values();
  
    /**
     * Attempt to retrieve a Status by text value 
     * @param text value 
     * @return status
     * @throws IllegalArgumentException if text is not found 
     */
    public static Status fromText( final String text )
      throws IllegalArgumentException
    {
      for ( final Status s : values )
      {
        if ( s.getText().equalsIgnoreCase( text ))
          return s;
      }

      throw new IllegalArgumentException( "Invalid value " + text );  
    }    
    
    
    /**
     * Turn ItemAckStatus into an AckStatus 
     * @param status status 
     * @return status
     */
    public static Status fromOrderItemAckStatus( final ItemAckStatus status )
    {
      switch( status )
      {
        case FULFILLABLE:
          return FULFILLABLE;
          
        case NOT_FULFILLABLE:
          return NO_INVENTORY;
          
        default:
          return NONE;
      }
    }
    
    /**
     * create
     * @param text jet text
     */
    Status( final String text )
    {
      this.text = text;
    }
        
    
    /**
     * Get the jet api text
     * @return jet text
     */
    public String getText()
    {
      return text;
    }
    
    
    @Override
    public String toString()
    {
      return text;
    }
  }
  
  /**
   * Merchant defined fulfillable or nonfulfillable skus within the order.
   */
  private final Status status;
  
  /**
   * Jet's unique identifier for an item in a merchant order.
   */
  private final String itemId;
  
  /**
   * Optional seller-supplied ID for an item in an order. If this value is 
   * specified with the Jet's order_item_id, Jet will map the two IDs and you 
   * can then use your own order item ID for subsequent feeds relating to that 
   * order item.
   */
  private final String altItemId;
  
  /**
   * A convenience field for the merchant sku
   */
  private final String merchantSku;
  
  
  /**
   * Create an instance of this from jet json 
   * @param json json
   * @return instance 
   */
  public static AckRequestItemRec fromJson( final JsonObject json )
  {
    Utils.checkNull( json, "json" );
    
    return new AckRequestItemRec(
      Status.fromText( json.getString( 
        "order_item_acknowledgement_status", "" )),
      json.getString( "order_item_id", "" ),
      json.getString( "alt_order_item_id", "" ),
      ""
    );
  }


  /**
   * Copy the properties from an OrderItemRec instance to an AckRequestItem
   * instance to send for the acknowledge command.
   * @param item order item 
   * @param ackStatus ack status to send 
   * @return ack item
   */
  public static AckRequestItemRec fromOrderItem( final OrderItemRec item, 
    final Status ackStatus )
  {
    Utils.checkNull( item, "item" );
    Utils.checkNull( ackStatus, "ackStatus" );
    
    return new AckRequestItemRec(
      ackStatus,
      item.getOrderItemId(),
      item.getAltOrderItemId(),
      item.getMerchantSku()
    );
  }

  
  
  /**
   * Copy the properties from an OrderItemRec instance to an AckRequestItem
   * instance to send for the acknowledge command.
   * @param item order item 
   * @return ack item
   */
  public static AckRequestItemRec fromOrderItem( final OrderItemRec item )
  {
    Utils.checkNull( item, "item" );
    
    return new AckRequestItemRec(
      Status.fromOrderItemAckStatus( item.getItemAckStatus()),
      item.getOrderItemId(),
      item.getAltOrderItemId(),
      item.getMerchantSku()
    );
  }
  
  
  /**
   * Create a new AckRequestItem instance.
   * @param status Merchant defined fulfillable or nonfulfillable skus 
   * within the order.
   * @param itemId Jet's unique identifier for an item in a merchant order.
   * @param altItemId Optional seller-supplied ID for an item in an order. If 
   * this value is specified with the Jet's order_item_id, Jet will map the two 
   * IDs and you can then use your own order item ID for subsequent feeds 
   * relating to that order item.
   * @param merchantSku a convenience field for the merchant sku.
   */
  public AckRequestItemRec(
    final Status status,
    final String itemId,
    final String altItemId,
    final String merchantSku
  ) {
    Utils.checkNull( status, "status" );
    Utils.checkNullEmpty( itemId, "itemId" );
    Utils.checkNull( altItemId, "altItemId" );
    Utils.checkNull( merchantSku, "merchantSku" );
    
    this.status = status;
    this.itemId = itemId;
    this.altItemId = altItemId;
    this.merchantSku = merchantSku;
  }
  
  
  /**
   * Get the merchant sku
   * This is only available when this record is created via fromOrderItem() or
   * if you specify it in the constructor.  This is NOT contained in any 
   * Jet Json.
   * @return sku 
   */
  public String getMerchantSku()
  {
    return merchantSku;
  }
  
  
  /**
   * Merchant defined fulfillable or nonfulfillable skus within the order.
   * @return value
   */
  public Status getStatus()
  {
    return status;
  }
  
  
  /**
   * Jet's unique identifier for an item in a merchant order.
   * @return value
   */
  public String getItemId()
  {
    return itemId;
  }
  
  
  /**
   * Optional seller-supplied ID for an item in an order. If this value is 
   * specified with the Jet's order_item_id, Jet will map the two IDs and you 
   * can then use your own order item ID for subsequent feeds relating to 
   * that order item.
   * @return value
   */
  public String getAltItemId()
  {
    return altItemId;
  }  
  
  
  /**
   * Turn this object into jet json 
   * @return json 
   */
  @Override
  public JsonObject toJSON()
  {
    return Json.createObjectBuilder()
      .add( "order_item_acknowledgement_status", status.getText())
      .add( "order_item_id", itemId )
      .add( "alt_order_item_id", altItemId )
      .build();
  }
}
