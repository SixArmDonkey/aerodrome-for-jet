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

import com.buffalokiwi.api.APIException;
import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetException;
import java.util.List;

/**
 * Represents the Jet Orders API 
 * @author John Quinn
 */
public interface IJetOrder 
{

  /**
   * Poll jet for direct cancel uri's and return the list of tokens
   * @param includePath Toggle including full uri
   * @return list
   * @throws APIException
   * @throws JetException
   * @deprecated
   */
  public List<String> getDirectCancelTokens(final boolean includePath) throws APIException, JetException;

  /**
   * Retrieve details about an order
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return detail
   * @throws APIException
   * @throws JetException
   */
  public OrderRec getOrderDetail(final String jetOrderId) throws APIException, JetException;

  /**
   * Poll Jet and retrieve a list of order status tokens for doing stuff
   * with orders.  This will ONLY return the jet order id string, NOT the
   * complete uri.
   * @param status status to poll
   * @return token list
   * @throws APIException
   * @throws JetException
   */
  public List<String> getOrderStatusTokens(final OrderStatus status) throws APIException, JetException;

  /**
   * Poll Jet and retrieve a list of order status tokens for doing stuff
   * with orders.
   * @param status status to poll
   * @param includePath If this is false, only the rightmost path part
   * is returned from the uri.
   * @return token list
   * @throws APIException
   * @throws JetException
   */
  public List<String> getOrderStatusTokens(final OrderStatus status, final boolean includePath) throws APIException, JetException;

  /**
   * Cancels an order.
   * @param curRec Full order details
   * @param altShipmentId
   * @return
   * @throws JetException
   * @throws APIException 
   */
  public IJetAPIResponse cancelOrder( final OrderRec curRec, final String altShipmentId ) throws JetException, APIException;  
  
  
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
  public List<String> pollOrdersByTag( final OrderStatus status, 
    final String tag, final boolean include ) throws JetException, APIException;  
}
