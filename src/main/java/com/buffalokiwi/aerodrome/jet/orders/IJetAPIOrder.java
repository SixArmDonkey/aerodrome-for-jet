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

import com.buffalokiwi.aerodrome.jet.IJetAPI;
import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.api.APIException;


/**
 *
 * @author john
 */
public interface IJetAPIOrder extends IJetAPI, IJetOrder
{
  /**
   * Poll Jet for some orders by status. 
   * @param status The jet status to poll 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendPollOrders( final OrderStatus status )
    throws APIException, JetException;
  
  
  /**
   * Poll jet for directed cancel uri's
   * @return response
   * @throws APIException
   * @throws JetException
   * @deprecated
   */
  public IJetAPIResponse sendPollDirectedCancel()
    throws APIException, JetException;
  
  
  /**
   * Retrieve details about an order 
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendGetOrderDetail( final String jetOrderId )
    throws APIException, JetException;
  
  
  /**
   * The order acknowledge call is utilized to allow a retailer to accept or 
   * reject an order. If there are any skus in the order that cannot be 
   * fulfilled then you will reject the order.
   * 
   * @param jetOrderId Jet-defined order id 
   * @param req The acknowledgement request to reply to jet with 
   * @return api response 
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPutAckOrder( final String jetOrderId, 
    final AckRequestRec req ) throws APIException, JetException;
  
  
  /**
   * The order shipped call is utilized to provide Jet with the SKUs that 
   * have been shipped or cancelled in an order, the tracking information, 
   * carrier information and any additional returns information for the order.
   * @param jetOrderId Jet-defined order id 
   * @param req shipment request 
   * @return api response 
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendPutShipOrder( final String jetOrderId,
    final ShipRequestRec req ) throws APIException, JetException;
  
  
 /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined 
   * with a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * 
   * Only one tag can be applied to a given merchant order id at a time. Thus, 
   * if one tag is applied and then later overwritten, the order will retain 
   * only the most recent tag.
   * To clear a tag, simply send a blank object.
   * 
   * @param orderId The jet defined order id 
   * @param tag Some arbitrary tag value 
   * @return Jet's response 
   * @throws JetException
   * @throws APIException 
   */
  public IJetAPIResponse sendPutTag( final String orderId, final String tag )
    throws JetException, APIException;
  
  
  /**
   * The PUT tagging functionality allows a user to: apply to an order a string 
   * of the user's choice; group SKUs by a common string; and when combined 
   * with a GET request for orders by status and tag, manage which orders are 
   * returned. It is generally meant to be used to achieve pseudo-pagination.
   * Using this endpoint you can access the first 1000 orders in a certain status. 
   * @param status Order status to query
   * @param tag Arbitrary tag value to query 
   * @param include Whether to include or exclude orders with the tag 
   * @return Response
   * @throws JetException
   * @throws APIException
   */
  public IJetAPIResponse sendGetTaggedOrders( final OrderStatus status, 
    final String tag, final boolean include ) throws JetException, APIException;  
}
