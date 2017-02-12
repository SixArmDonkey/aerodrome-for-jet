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

  
}
