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
 * The Jet Returns API interface
 * @author John Quinn
 */
public interface IJetReturn 
{
  /**
   * Poll Jet and retrieve a list of returns id's for doing stuff
   * with returns.  This will ONLY return the jet return id string, NOT the
   * complete uri.
   * @param status status to poll
   * @return token list
   * @throws APIException
   * @throws JetException
   */
  public List<String> getReturnsStatusTokens(final ReturnStatus status) 
          throws APIException, JetException;

  /**
   * Retrieve detail about a specific return
   * @param jetReturnId Jet return id
   * @return
   * @throws APIException
   * @throws JetException
   */
  public ReturnRec getReturnDetail(final String jetReturnId) 
          throws APIException, JetException;

  /**
   * Poll Jet and retrieve a list of return id's for doing stuff
   * with returns.
   * @param status status to poll
   * @param includePath If this is false, only the rightmost path part
   * is returned from the uri.
   * @return token list
   * @throws APIException
   * @throws JetException
   */
  public List<String> getReturnsStatusTokens(final ReturnStatus status, 
          final boolean includePath) throws APIException, JetException;
  
  /**
   * Send a complete return command to jet
   * @param jetReturnId
   * @param payload The payload
   * @return api response
   * @throws APIException
   * @throws JetException
   */
  public boolean completeReturn(final String jetReturnId, 
      final CompleteReturnRequestRec request) throws APIException, JetException;  
}
