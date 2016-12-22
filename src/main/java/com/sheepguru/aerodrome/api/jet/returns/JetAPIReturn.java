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

package com.sheepguru.aerodrome.api.jet.returns;

import com.sheepguru.aerodrome.api.APIException;
import com.sheepguru.aerodrome.api.IAPIHttpClient;
import com.sheepguru.aerodrome.api.jet.IJetAPIResponse;
import com.sheepguru.aerodrome.api.jet.JetAPI;
import com.sheepguru.aerodrome.api.jet.JetConfig;
import com.sheepguru.aerodrome.api.jet.JetException;
import com.sheepguru.aerodrome.api.jet.Utils;
import java.util.List;


/**
 * The Jet.com Returns API 
 * 
 * @author John Quinn
 */
public class JetAPIReturn extends JetAPI
{
  /**
   * Create a new API instance
   * @param client The built APIHttpClient instance 
   * @param conf The Jet Configuration object
   */
  public JetAPIReturn( final IAPIHttpClient client, final JetConfig conf )
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
  public JetAPIReturn( final IAPIHttpClient client, final JetConfig conf, final boolean lockHost )
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
  public JetAPIReturn( final IAPIHttpClient client, final JetConfig conf, 
    final boolean lockHost, final long maxDownloadSize ) 
  {
    super( client, conf, lockHost, maxDownloadSize );
  }
  
  
  /**
   * Check for any returns and return a list of returns id's 
   * @param status
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendPollReturns( final ReturnStatus status )
    throws APIException, JetException
  {
    Utils.checkNull( status, "status" );
    
    if ( status == ReturnStatus.NONE )
      throw new IllegalArgumentException( "status cannot be ReturnStatus.NONE" );
    
    return get(
      config.getGetReturnsUrl( status.getText()),
      getJSONHeaderBuilder().build()
    );
  }
  

  /**
   * Poll Jet and retrieve a list of returns id's for doing stuff
   * with returns.  This will ONLY return the jet return id string, NOT the 
   * complete uri.
   * @param status status to poll
   * @return token list 
   * @throws APIException
   * @throws JetException 
   */
  public List<String> getOrderStatusTokens( final ReturnStatus status ) 
    throws APIException, JetException    
  {    
    return getReturnStatusTokens( status, false );
  } 
  
  
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
  public List<String> getReturnStatusTokens( final ReturnStatus status, 
    final boolean includePath ) throws APIException, JetException    
  {    
    Utils.checkNull( status, "status" );
    return jsonArrayToTokenList( sendPollReturns( status )
      .getJsonObject().getJsonArray( "return_urls" ), includePath );
  }
  
  
  /**
   * Retrieve detail about a specific return 
   * @param jetReturnId Jet return id 
   * @return
   * @throws APIException
   * @throws JetException 
   */
  public IJetAPIResponse sendGetReturnDetail( final String jetReturnId )
    throws APIException, JetException
  {
    Utils.checkNullEmpty( jetReturnId, "jetReturnId" );
    
    return get(
      config.getGetReturnDetailUrl( jetReturnId ),
      getJSONHeaderBuilder().build()
    );
  }
}
