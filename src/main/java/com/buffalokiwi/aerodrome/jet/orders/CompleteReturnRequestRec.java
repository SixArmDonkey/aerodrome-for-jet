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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * The record used to send as a request for completing a return.
 * Send this to jet put complete return command.
 * 
 * @author John Quinn
 */
public class CompleteReturnRequestRec implements Jsonable
{
  /**
   * This field set by order refund message. Specifies if the merchant 
   * agrees to the return charge for the return notification.
   */
  private final boolean agreeToReturnCharge;
  
  /**
   * Optional merchant supplied order ID that should have been established in 
   * OrderAcknowledgement in order to use in this message.
   */
  private final String altOrderId;

  /**
   * Jet's unique ID for a given merchant order
   */
  private final String merchantOrderId;

  /**
   * The reason the merchant does not agree to the return charge 
   * for the return notification.
   */
  private final ChargeFeedback feedback;

  /**
   * This is included if status is inprogress or completed.
   */
  private final List<ReturnItemRec> items;
  
  
  /**
   * Turn jet json into an instance 
   * @param json json
   * @return instance 
   */
  public static CompleteReturnRequestRec fromJson( final JsonObject json )
  {
    return new CompleteReturnRequestRec(
      json.getString( "merchant_order_id", "" ),
      json.getString( "alt_order_id", "" ),
      json.getBoolean( "agree_to_return_charge", false ),
      ChargeFeedback.fromText( json.getString( "return_charge_feedback", "" )),
      jsonToReturnItemsList( json.getJsonArray( "items" ))
   );
  }
  
  
  /**
   * Turn jet json into a list of ReturnItemRec instances
   * @param a array 
   * @return list 
   */
  private static List<ReturnItemRec> jsonToReturnItemsList( final JsonArray a )
  {
    final List<ReturnItemRec> items = new ArrayList<>();
    if ( a != null )
    {
      for ( int i = 0; i < a.size(); i++ )
      {
        items.add( ReturnItemRec.fromJson( a.getJsonObject( i )));
      }
    }    
    
    return items;
  }
  
  
  /**
   * Construct it, Dog
   * @param agreeToReturnCharge This field set by order refund message. 
   * Specifies if the merchant agrees to the return charge for the return 
   * notification.
   * @param altOrderId Optional merchant supplied order ID that should have 
   * been established in OrderAcknowledgement in order to use in this message.
   * @param merchantOrderId Jet's unique ID for a given merchant order
   * @param feedback The reason the merchant does not agree to the return charge 
   * for the return notification.
   * @param items This is included if status is inprogress or completed.
   */
  public CompleteReturnRequestRec(
    final String merchantOrderId,
    final String altOrderId,
    final boolean agreeToReturnCharge,
    final ChargeFeedback feedback,
    final List<ReturnItemRec> items
  ) {
    Utils.checkNull( altOrderId, "altOrderId" );
    Utils.checkNullEmpty( merchantOrderId, "merchantOrderId" );
    Utils.checkNull( feedback, "feedback" );
    Utils.checkNull( items, "items" );
    
    
    this.agreeToReturnCharge = agreeToReturnCharge;
    this.altOrderId = altOrderId;
    this.merchantOrderId = merchantOrderId;
    this.feedback = feedback;
    this.items = Collections.unmodifiableList( items );
  }

  /**
   * This field set by order refund message. Specifies if the merchant 
   * agrees to the return charge for the return notification.
   * @return the agreeToReturnCharge
   */
  public boolean isAgreeToReturnCharge() 
  {
    return agreeToReturnCharge;
  }

  
  /**
   * Optional merchant supplied order ID that should have been established in 
   * OrderAcknowledgement in order to use in this message.
   * @return the altOrderId
   */
  public String getAltOrderId() 
  {
    return altOrderId;
  }

  
  /**
   * Jet's unique ID for a given merchant order
   * @return the merchantOrderId
   */
  public String getMerchantOrderId() 
  {
    return merchantOrderId;
  }

  
  /**
   * The reason the merchant does not agree to the return charge 
   * for the return notification.
   * @return the feedback
   */
  public ChargeFeedback getFeedback() 
  {
    return feedback;
  }

  
  /**
   * This is included if status is inprogress or completed.
   * @return the items
   */
  public List<ReturnItemRec> getItems() 
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
    return Json.createObjectBuilder()
      .add( "merchant_order_id", merchantOrderId )
      .add( "alt_order_id", altOrderId )
      .add( "agree_to_return_charge", agreeToReturnCharge )
      .add( "return_charge_feedback", feedback.getText())
      .add( "items", Utils.jsonableToArray( items ))
      .build();
  }
}
