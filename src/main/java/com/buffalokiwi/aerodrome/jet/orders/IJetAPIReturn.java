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
public interface IJetAPIReturn extends IJetAPI, IJetReturn
{
  /**
   * Retrieve detail about a specific return
   * @param jetReturnId Jet return id
   * @return
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetReturnDetail(final String jetReturnId) 
          throws APIException, JetException;

  /**
   * Check for any returns and return a list of returns id's
   * @param status
   * @return
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPollReturns(final ReturnStatus status) 
          throws APIException, JetException;

  /**
   * Send a complete return command to jet
   * @param jetReturnId
   * @param payload The payload
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendPutCompleteReturn(final String jetReturnId, 
          final String payload) throws APIException, JetException;  
  
  /**
   * Send a complete return command to jet 
   * @param jetReturnId
   * @param payload The payload 
   * @return api response
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse putCompleteReturn( final String jetReturnId,
    final CompleteReturnRequestRec request ) throws APIException, JetException;  
}
