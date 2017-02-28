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

import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.api.APIException;
import java.util.List;

/**
 *
 * @author john
 */
public interface IJetAPIRefund extends IJetAPIReturn, IJetRefund
{

  /**
   * Get details about a refund
   * @param refundAuthId auth id for the refund
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetRefundDetail( final String refundAuthId ) 
    throws APIException, JetException;

  /**
   * Poll for refunds
   * @param status Status to poll
   * @return api response
   */
  public IJetAPIResponse sendPollRefunds( final String status ) 
    throws APIException, JetException;

  /**
   * Merchant Initiated Refunds can be created using this API method
   * @param orderId The Order ID you wish to create a refund for.
   * @param altRefundId The Alternative Refund ID that you must create for a
   * refund.
   * @param items A list of items that are included in the refund from the order
   * being refunded.
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPostCreateRefund( final String orderId, 
    final String altRefundId, final List<RefundItemRec> items ) 
    throws APIException, JetException;
    
}
