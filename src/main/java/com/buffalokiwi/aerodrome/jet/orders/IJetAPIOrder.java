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
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIHttpClient;
import java.util.List;

/**
 *
 * @author john
 */
public interface IJetAPIOrder extends IJetAPI
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
   * Poll Jet and retrieve a list of order status tokens for doing stuff
   * with orders.  This will ONLY return the jet order id string, NOT the 
   * complete uri.
   * @param status status to poll
   * @return token list 
   * @throws APIException
   * @throws JetException 
   */
  public List<String> getOrderStatusTokens( final OrderStatus status ) 
    throws APIException, JetException;    

  
  
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
  public List<String> getOrderStatusTokens( final OrderStatus status, 
    final boolean includePath ) throws APIException, JetException;    
 
  
  
  /**
   * Poll jet for directed cancel uri's
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPollDirectedCancel()
    throws APIException, JetException;
  
  
  
  /**
   * Poll jet for direct cancel uri's and return the list of tokens 
   * @param includePath Toggle including full uri 
   * @return list 
   * @throws APIException
   * @throws JetException 
   */
  public List<String> getDirectCancelTokens( final boolean includePath )
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
   * Retrieve details about an order 
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return detail
   * @throws APIException
   * @throws JetException 
   */
  public OrderRec getOrderDetail( final String jetOrderId )
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
}
