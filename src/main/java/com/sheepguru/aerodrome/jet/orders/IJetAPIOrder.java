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
package com.sheepguru.aerodrome.jet.orders;

import com.sheepguru.aerodrome.jet.IJetAPI;
import com.sheepguru.aerodrome.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.jet.JetException;
import com.sheepguru.api.APIException;

/**
 *
 * @author john
 */
public interface IJetAPIOrder extends IJetAPI
{

  /**
   * Retrieve details about an order
   * @param jetOrderId Jet order id (need to poll for these first)
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetOrderDetail(final String jetOrderId) throws APIException, JetException;

  /**
   * Poll jet for directed cancel uri's
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPollDirectedCancel() throws APIException, JetException;

  /**
   * Poll Jet for some orders by status.
   * @param status The jet status to poll
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPollOrders(final OrderStatus status) throws APIException, JetException;

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
  public IJetAPIResponse sendPutAckOrder(final String jetOrderId, final AckRequestRec req) throws APIException, JetException;

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
  public IJetAPIResponse sendPutShipOrder(final String jetOrderId, final ShipRequestRec req) throws APIException, JetException;  
}
