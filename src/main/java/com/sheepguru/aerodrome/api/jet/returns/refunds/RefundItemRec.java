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
package com.sheepguru.aerodrome.api.jet.returns.refunds;

import com.sheepguru.aerodrome.api.jet.Utils;
import com.sheepguru.aerodrome.api.jet.returns.RefundAmountRec;
import com.sheepguru.aerodrome.api.jet.returns.RefundFeedback;
import com.sheepguru.aerodrome.api.jet.returns.ReturnItemRec;
import com.sheepguru.aerodrome.api.jet.returns.ReturnReason;
import java.math.BigDecimal;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 *
 * @author john
 */
public class RefundItemRec extends ReturnItemRec
{
  /**
   * The reason the customer initiated the return.
   */
  private final ReturnReason refundReason;
  
  
  public static class Builder extends ReturnItemRec.Builder
  {

    /**
     * The reason the customer initiated the return.
     */
    private ReturnReason refundReason = ReturnReason.NONE;
    
    
    /**
     * The reason the customer initiated the return.
     */
    public Builder setRefundReason( final ReturnReason refundReason )
    {
      Utils.checkNull( refundReason, "refundReason" );
      this.refundReason = refundReason;
      return this;
    }
  
    /**
     * Build the object
     * @return instance
     */
    @Override
    public RefundItemRec build()
    {
      return new RefundItemRec( this );
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
    b.setQtyReturned( r.getQtyReturned());
    b.setOrderReturnRefundQty( r.getOrderReturnRefundQty());
    b.setFeedback( r.getFeedback());
    b.setNotes( r.getNotes());
    b.setAmount( r.getAmount());    
    b.setRefundReason( ReturnReason.fromText( json.getString( "refund_reason", "" )));
    return b.build();
  }  
  
  
  
  /**
   * Constructor 
   * @param b builder 
   */
  protected RefundItemRec( final Builder b )
  {
    super( b );
    this.refundReason = b.refundReason;
  }
  
  
  /**
   * The reason the customer initiated the return.
   * @return reason
   */
  public ReturnReason getRefundReason()
  {
    return refundReason;
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
      b.add( entry.getKey(), entry.getValue());      
    }
    
    b.add(  "refund_reason", refundReason.getText());
    
    return b.build();
  }  
}
