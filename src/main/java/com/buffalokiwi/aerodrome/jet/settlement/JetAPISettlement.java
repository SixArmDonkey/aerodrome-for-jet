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
package com.buffalokiwi.aerodrome.jet.settlement;

import com.buffalokiwi.aerodrome.jet.IJetAPIResponse;
import com.buffalokiwi.aerodrome.jet.JetAPI;
import com.buffalokiwi.aerodrome.jet.JetConfig;
import com.buffalokiwi.aerodrome.jet.JetException;
import com.buffalokiwi.aerodrome.jet.Utils;
import com.buffalokiwi.api.APIException;
import com.buffalokiwi.api.IAPIHttpClient;
import java.util.List;


/**
 * Jet Settlement API.
 * @author John Quinn
 */
public class JetAPISettlement extends JetAPI implements IJetAPISettlement
{
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPISettlement( final IAPIHttpClient client, final JetConfig conf )
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
  public JetAPISettlement( final IAPIHttpClient client, 
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
  public JetAPISettlement( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize )
  {
    super( client, conf, lockHost, maxDownloadSize );
  }
  
  
  /**
   * Retrieve a list of uri's for retrieving settlement reports.
   * @param days The number of days from today that you'd like to retrieve 
   * settlement reports
   * @return response
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetSettlementDays( final int days )
    throws APIException, JetException
  {
    Utils.checkIntGTZ( days, "days" );
    
    return get(
      config.getGetSettlementDaysUrl( days ),
      getJSONHeaderBuilder().build()
    );
  }
  
  
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
  @Override
  public List<String> getSettlementDays( final int days, final boolean includePath )
    throws APIException, JetException
  {
    return jsonArrayToTokenList( sendGetSettlementDays( days ).getJsonObject()
      .getJsonArray( "settlement_report_urls" ), includePath );
  }
  
  
  /**
   * Retrieve a list of uri's for retrieving settlement reports.
   * @param days The number of days from today that you'd like to retrieve 
   * settlement reports
   * @return report uri's 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public List<String> getSettlementDays( final int days )
    throws APIException, JetException
  {
    return getSettlementDays( days, false );
  }
  
  
  /**
   * Retrieve a settlement report
   * @param id report id 
   * @return response 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public IJetAPIResponse sendGetSettlementReport( final String id )
    throws APIException, JetException
  {
    return get(
      config.getGetSettlementReportUrl( id ),
      getJSONHeaderBuilder().build()
    );
  }
  
    
  /**
   * Retrieve a settlement report
   * @param id report id 
   * @return report object 
   * @throws APIException
   * @throws JetException 
   */
  @Override
  public SettlementRec getSettlementReport( final String id )
    throws APIException, JetException
  {
    return SettlementRec.fromJson( sendGetSettlementReport( id )
      .getJsonObject());
  }
}
