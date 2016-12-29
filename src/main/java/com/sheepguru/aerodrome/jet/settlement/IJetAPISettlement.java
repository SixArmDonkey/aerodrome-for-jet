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
package com.sheepguru.aerodrome.jet.settlement;

import com.sheepguru.aerodrome.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.jet.JetException;
import com.sheepguru.api.APIException;
import java.util.List;


/**
 * Settlement API 
 * @author John Quinn
 */
public interface IJetAPISettlement 
{
  /**
   * Retrieve a list of uri's for retrieving settlement reports.
   * @param days The number of days from today that you'd like to retrieve
   * settlement reports
   * @param includePath Toggle returning the entire path or just the rightmost
   * part. (you only need the right side)
   * @return report uri's
   * @throws APIException
   * @throws JetException
   */
  public List<String> getSettlementDays( final int days, 
    final boolean includePath ) throws APIException, JetException;

  
  /**
   * Retrieve a list of uri's for retrieving settlement reports.
   * @param days The number of days from today that you'd like to retrieve
   * settlement reports
   * @return report uri's
   * @throws APIException
   * @throws JetException
   */
  public List<String> getSettlementDays( final int days ) 
    throws APIException, JetException;

  
  /**
   * Retrieve a settlement report
   * @param id report id
   * @return report object
   * @throws APIException
   * @throws JetException
   */
  public SettlementRec getSettlementReport( final String id ) 
    throws APIException, JetException;

  
  /**
   * Retrieve a list of uri's for retrieving settlement reports.
   * @param days The number of days from today that you'd like to retrieve
   * settlement reports
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetSettlementDays( final int days ) 
    throws APIException, JetException;

  
  /**
   * Retrieve a settlement report
   * @param id report id
   * @return response
   * @throws APIException
   * @throws JetException
   */
  public IJetAPIResponse sendGetSettlementReport( final String id ) 
    throws APIException, JetException;  
}
