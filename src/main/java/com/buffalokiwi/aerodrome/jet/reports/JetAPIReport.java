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
import com.buffalokiwi.aerodrome.jet.JetAPI;
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIHttpClient;

/**
 * Bulk Reporting API 
 * @author John Quinn
 */
public class JetAPIReport extends JetAPI implements IJetAPIReport 
{
 /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIReport( final IAPIHttpClient client, final JetConfig conf )
  {    
    super( client, conf );
  }

  
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   */
  public JetAPIReport( final IAPIHttpClient client, 
    final JetConfig conf, final boolean lockHost )
  {
    super( client, conf, lockHost );
  }
  
    
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   * @param lockHost Toggle locking the host to a domain if http is not present
   * in the url string.
   * @param maxDownloadSize Set a maximum download site for the local client.
   * This is a fixed limit.
   */
  public JetAPIReport( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize )
  {
    super( client, conf, lockHost, maxDownloadSize );
  }  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param type Report Type
   * @return Report Id 
   * @throws JetException
   * @throws APIException 
   */
  @Override
  public String createReport( final ReportType type )
    throws JetException, APIException
  {
    return sendPostNewReport( type ).getJsonObject().getString( "report_id", "" );
  }
  
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param reportId The Jet defined report ID associated with the requested report
   * @return Report status 
   * @throws JetException
   * @throws APIException 
   */  
  @Override
  public ReportStatusRec getReportStatus( final String reportId )
    throws JetException, APIException
  {
    return ReportStatusRec.fromJSON( reportId, sendGetReportStatus( reportId ).getJsonObject());
  }
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param type Report Type
   * @return Response containing report id 
   * @throws JetException
   * @throws APIException 
   */
  @Override
  public IJetAPIResponse sendPostNewReport( final ReportType type ) 
    throws JetException, APIException
  {
    Utils.checkNull( type, "type" );
    return post( 
      config.getPostCreateReportUrl( type.getText()), 
      "", 
      getJSONHeaderBuilder().build()
    );
  }
  
  
  /**
   * The bulk reporting feature allows the merchant to request a report which 
   * will return to the merchant a JSON file with the requested information. 
   * Jet.com will use this to pass data to the retailer
   * @param reportId The Jet defined report ID associated with the requested report
   * @return Report status 
   * @throws JetException
   * @throws APIException 
   */
  @Override
  public IJetAPIResponse sendGetReportStatus( final String reportId )
    throws JetException, APIException
  {
    Utils.checkNullEmpty( reportId, "reportId" );
    return get( 
      config.getGetReportStatusUrl( reportId ), 
      getJSONHeaderBuilder().build()
    );
  }
}
