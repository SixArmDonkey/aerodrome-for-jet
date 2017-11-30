/**
 * This file is part of the Aerodrome package, and is subject to the 
 * terms and conditions defined in file 'LICENSE', which is part 
 * of this source code package.
 *
 * Copyright (c) 2017 All Rights Reserved, John T. Quinn III,
 * <johnquinn3@gmail.com>
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */


package com.buffalokiwi.aerodrome.jet.reports;

import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.api.APIException;

/**
 *
 * @author John Quinn
 */
public interface IJetAPIReport 
{
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param type Report Type
   * @return Report Id 
   * @throws JetException
   * @throws APIException 
   */
  public String createReport( final ReportType type )
    throws JetException, APIException;
  
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param reportId The Jet defined report ID associated with the requested report
   * @return Report status 
   * @throws JetException
   * @throws APIException 
   */  
  public ReportStatusRec getReportStatus( final String reportId )
    throws JetException, APIException;
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param type Report Type
   * @return Response containing report id 
   * @throws JetException
   * @throws APIException 
   */
  public IJetAPIResponse sendPostNewReport( final ReportType type ) 
    throws JetException, APIException;
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param reportId The Jet defined report ID associated with the requested report
   * @return Report status 
   * @throws JetException
   * @throws APIException 
   */
  public IJetAPIResponse sendGetReportStatus( final String reportId )
    throws JetException, APIException;
}
